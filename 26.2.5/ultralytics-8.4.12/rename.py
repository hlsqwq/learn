import os
import shutil

# 定义源文件夹和目标文件夹
src_dir = r'C:\python\code\ultralytics-8.4.12\datasets'
dst_dir = r'C:\python\code\ultralytics-8.4.12\datasets\pic'

# 如果目标文件夹不存在，则创建它
if not os.path.exists(dst_dir):
    os.makedirs(dst_dir)
    print(f"创建文件夹: {dst_dir}")

# 支持的图片格式
valid_extensions = ('.jpg', '.jpeg', '.png', '.bmp', '.webp')

# 获取源目录下所有的图片文件
files = [f for f in os.listdir(src_dir) if f.lower().endswith(valid_extensions)]

print(f"发现 {len(files)} 张图片，开始搬运...")

# 计数器
count = 1

for filename in files:
    # 获取文件后缀名（如 .png）
    ext = os.path.splitext(filename)[1]

    # 构建源文件路径
    src_path = os.path.join(src_dir, filename)

    # 构建目标文件名和路径 (例如: 1.png, 2.png...)
    new_name = f"{count}{ext}"
    dst_path = os.path.join(dst_dir, new_name)

    # 执行移动操作（如果想保留原图，请将 move 改为 copy）
    shutil.move(src_path, dst_path)

    print(f"已移动: {filename} -> {new_name}")
    count += 1

print(f"--- 任务完成，共处理 {count - 1} 张图片 ---")