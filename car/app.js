// 引入核心依赖
const mqtt = require("mqtt");
const express = require("express");
const cors = require("cors");
const initSqlJs = require("sql.js");
const fs = require("fs");
const path = require("path");

// 初始化 Express 实例
const app = express();
app.use(cors());
app.use(express.json());
const PORT = 3000;
const DB_FILE = path.join(__dirname, "sensor.db");

// MQTT 配置
const MQTT_BROKER = "mqtt://192.168.0.2:1883";
const MQTT_TOPIC = "demo";
const MQTT_QRCODE_TOPIC = "qrcode"; // 新增：二维码订阅主题

// 全局数据库实例
let db;

// --- 新增：获取当前时间工具函数 (yyyy-MM-dd HH:mm:ss) ---
function getCurrentDateTime() {
  const now = new Date();
  const pad = (n) => n.toString().padStart(2, "0");
  return `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())} ${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}`;
}

// 1. 初始化 SQL.js 数据库
async function initDb() {
  try {
    const SQL = await initSqlJs({
      locateFile: (file) => `node_modules/sql.js/dist/${file}`,
    });

    let dbData = [];
    if (fs.existsSync(DB_FILE)) {
      dbData = fs.readFileSync(DB_FILE);
    }

    db = new SQL.Database(dbData);
    console.log("SQL.js 数据库初始化成功");

    // 初始化 sensor 表
    const createSensorTableSql = `
      CREATE TABLE IF NOT EXISTS sensor (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        temperature REAL NOT NULL DEFAULT 0,
        humidity REAL NOT NULL DEFAULT 0,
        illumination REAL NOT NULL DEFAULT 0,
        create_time DATETIME NOT NULL DEFAULT '1970-01-01 00:00:00'
      )
    `;
    db.run(createSensorTableSql);

    // --- 新增：初始化 qrcode_info 表 ---
    const createQrcodeTableSql = `
      CREATE TABLE IF NOT EXISTS qrcode_info (
        db_id INTEGER PRIMARY KEY AUTOINCREMENT,
        id TEXT NOT NULL,
        terminal TEXT NOT NULL,
        departure TEXT NOT NULL,
        create_time DATETIME NOT NULL
      )
    `;
    db.run(createQrcodeTableSql);
    console.log(`sensor 表和 qrcode_info 表初始化成功`);

    saveDbToFile();
  } catch (err) {
    console.error("数据库/表初始化失败：", err.message);
  }
}

// 2. 保存数据库到本地文件
function saveDbToFile() {
  try {
    const data = db.export();
    const buffer = Buffer.from(data);
    fs.writeFileSync(DB_FILE, buffer);
  } catch (err) {
    console.error("数据库持久化失败：", err.message);
  }
}

// 3. 工具函数
function validateNumber(value) {
  const num = Number(value);
  return isNaN(num) ? 0 : num;
}

function validateDateTime(value) {
  if (
    typeof value !== "string" ||
    !/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/.test(value)
  ) {
    return "1970-01-01 00:00:00";
  }
  return value;
}

// 4. 初始化 MQTT 客户端
const mqttClient = mqtt.connect(MQTT_BROKER, {
  keepalive: 60,
  reconnectPeriod: 1000,
  connectTimeout: 30000,
});

// MQTT 连接成功
mqttClient.on("connect", () => {
  console.log(`MQTT 客户端连接成功：${MQTT_BROKER}`);
  // --- 修改：同时订阅两个主题 ---
  mqttClient.subscribe([MQTT_TOPIC, MQTT_QRCODE_TOPIC], { qos: 0 }, (err) => {
    if (err) {
      console.error(`订阅 MQTT 主题失败：`, err.message);
    } else {
      console.log(
        `已成功订阅 MQTT 主题：${MQTT_TOPIC} 和 ${MQTT_QRCODE_TOPIC}`,
      );
    }
  });
});

// MQTT 接收消息
mqttClient.on("message", (topic, payload) => {
  const payloadStr = payload.toString("utf8");

  // --- 修改：根据主题分支处理不同的数据逻辑 ---
  if (topic === MQTT_TOPIC) {
    // 处理传感器数据
    try {
      const sensorMsg = JSON.parse(payloadStr);
      const temperature = validateNumber(sensorMsg.temperature);
      const humidity = validateNumber(sensorMsg.humidity);
      const illumination = validateNumber(sensorMsg.illumination);
      const createTime = validateDateTime(sensorMsg.createTime);

      const insertStmt = db.prepare(`
        INSERT INTO sensor (temperature, humidity, illumination, create_time)
        VALUES (?, ?, ?, ?)
      `);
      insertStmt.run([temperature, humidity, illumination, createTime]);
      insertStmt.free();
      saveDbToFile();
      console.log(`传感器数据入库成功：温度=${temperature}`);
    } catch (err) {
      console.error("传感器消息处理失败：", err.message);
    }
  } else if (topic === MQTT_QRCODE_TOPIC) {
    // --- 新增：处理二维码数据 ---
    try {
      console.log(`收到 MQTT 二维码消息：${payloadStr}`);
      const qrMsg = JSON.parse(payloadStr);

      const qrcodeId = qrMsg.id || "";
      const terminal = qrMsg.terminal || "";
      const departure = qrMsg.departure || "";
      const createTime = getCurrentDateTime(); // 系统自动生成当前时间

      const insertStmt = db.prepare(`
        INSERT INTO qrcode_info (id, terminal, departure, create_time)
        VALUES (?, ?, ?, ?)
      `);
      insertStmt.run([qrcodeId, terminal, departure, createTime]);
      insertStmt.free();

      saveDbToFile();
      console.log(`二维码数据入库成功：ID=${qrcodeId}, 时间=${createTime}`);
    } catch (err) {
      console.error("二维码消息处理失败（请确保为合法JSON）：", err.message);
    }
  }
});

mqttClient.on("error", (err) => {
  console.error(`MQTT 连接错误：${err.message}`);
  mqttClient.reconnect();
});

// 5. 工具函数：查询 sensor 表
function querySensorAll(sql, params = []) {
  return new Promise((resolve, reject) => {
    try {
      const stmt = db.prepare(sql);
      const results = [];
      while (stmt.step()) {
        const row = stmt.getAsObject();
        results.push({
          id: row.id,
          temperature: row.temperature,
          humidity: row.humidity,
          illumination: row.illumination,
          createTime: row.create_time,
        });
      }
      stmt.free();
      resolve(results);
    } catch (err) {
      reject(err);
    }
  });
}

// 接口 1：查询最近 1 条传感器数据
app.get("/api/sensor/latest/1", async (req, res) => {
  try {
    const rows = await querySensorAll(
      `SELECT * FROM sensor ORDER BY create_time DESC, id DESC LIMIT 1`,
    );
    res.json({
      code: 200,
      msg: "查询成功",
      data: rows.length > 0 ? rows[0] : null,
    });
  } catch (err) {
    res.status(500).json({ code: 500, msg: "查询失败", error: err.message });
  }
});

// 接口 2：查询最近 30 条传感器数据
app.get("/api/sensor/latest/30", async (req, res) => {
  try {
    const rows = await querySensorAll(
      `SELECT * FROM sensor ORDER BY create_time DESC, id DESC LIMIT 30`,
    );
    res.json({ code: 200, msg: "查询成功", count: rows.length, data: rows });
  } catch (err) {
    res.status(500).json({ code: 500, msg: "查询失败", error: err.message });
  }
});

// --- 新增接口：查询最新的一条二维码识别信息 ---
app.get("/api/qrcode/latest", (req, res) => {
  try {
    const stmt = db.prepare(`
      SELECT * FROM qrcode_info
      ORDER BY create_time DESC, db_id DESC
      LIMIT 1
    `);

    const rows = [];
    while (stmt.step()) {
      const row = stmt.getAsObject();
      rows.push({
        dbId: row.db_id,
        id: row.id,
        terminal: row.terminal,
        departure: row.departure,
        createTime: row.create_time,
      });
    }
    stmt.free();

    res.json({
      code: 200,
      msg: "查询成功",
      data: rows.length > 0 ? rows[0] : null,
    });
  } catch (err) {
    res.status(500).json({
      code: 500,
      msg: "查询失败",
      error: err.message,
    });
  }
});

// 7.5 新增接口：前端发送指令到 MQTT 的 cmd 主题
app.post("/api/sensor/cmd", (req, res) => {
  try {
    // 获取前端传来的请求体
    const payload = req.body;

    // 基础校验：检查请求体是否为空
    if (!payload || Object.keys(payload).length === 0) {
      return res.status(400).json({
        code: 400,
        msg: "指令下发失败：请求体不能为空",
      });
    }

    // 将 JSON 对象转换为字符串以便通过 MQTT 发送
    const messageStr = JSON.stringify(payload);
    const CMD_TOPIC = "demo";

    // 使用现有的 mqttClient 发布消息
    mqttClient.publish(CMD_TOPIC, messageStr, { qos: 0 }, (err) => {
      if (err) {
        console.error(`发送指令到主题 ${CMD_TOPIC} 失败：`, err.message);
        return res.status(500).json({
          code: 500,
          msg: "指令下发失败",
          error: err.message,
        });
      }

      console.log(`成功下发指令到主题 ${CMD_TOPIC}：${messageStr}`);
      res.json({
        code: 200,
        msg: "指令下发成功",
        data: payload,
      });
    });
  } catch (err) {
    console.error("处理下发指令发生异常：", err.message);
    res.status(500).json({
      code: 500,
      msg: "服务器内部异常",
      error: err.message,
    });
  }
});

// 8. 启动服务
async function startServer() {
  await initDb();
  app.listen(PORT, () => {
    console.log(`接口服务已启动：http://localhost:${PORT}`);
    console.log(
      "接口 1：最近 1 条传感器数据 → http://localhost:3000/api/sensor/latest/1",
    );
    console.log(
      "接口 2：最近 30 条传感器数据 → http://localhost:3000/api/sensor/latest/30",
    );
    console.log(
      "接口 3：最近 1 条二维码信息 → http://localhost:3000/api/qrcode/latest",
    ); // 新增日志

    console.log(
      "接口 4 (POST)：下发控制指令   → http://localhost:3000/api/sensor/cmd",
    );
  });
}

// 9. 进程退出时清理资源
process.on("SIGINT", () => {
  saveDbToFile();
  console.log("数据库已持久化到文件");
  mqttClient.end(() => {
    console.log("MQTT 客户端已断开");
    process.exit(0);
  });
});

startServer();
