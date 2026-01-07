package com.hls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 中国象棋游戏 - 最终修复版（画布延伸100px+坐标精准交互）
 * 特性：
 * 1. 画布往外延伸100px，内部格子50px大小不变
 * 2. 精准校准鼠标交互坐标，解决选不到棋子的问题
 * 3. 楚河汉界4、5行横线完整无断线
 * 4. 完整的走棋规则、轮流走棋、胜负判定
 */
public class ChineseChessGame {
    // 内部格子间距（保持50px不变）
    private static final int GRID_SIZE = 50;
    // 交叉点列数（9列：0-8）
    private static final int COLUMNS = 9;
    // 交叉点行数（10行：0-9）
    private static final int ROWS = 10;
    // 原始棋盘尺寸（内部格子区域）
    private static final int ORIG_BOARD_WIDTH = (COLUMNS - 1) * GRID_SIZE;
    private static final int ORIG_BOARD_HEIGHT = (ROWS - 1) * GRID_SIZE;
    // 画布延伸量（上下左右各100px）
    private static final int EXTEND_SIZE = 100;
    // 扩展后的面板尺寸（原始尺寸 + 2*延伸量）
    private static final int PANEL_WIDTH = ORIG_BOARD_WIDTH + 2 * EXTEND_SIZE;
    private static final int PANEL_HEIGHT = ORIG_BOARD_HEIGHT + 2 * EXTEND_SIZE;
    // 走棋提示栏高度
    private static final int TIP_BAR_HEIGHT = 35;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("中国象棋 - 精准交互版");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            // 主面板
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.setBackground(Color.WHITE);

            // 棋盘面板（扩展后的尺寸）
            ChessBoard chessBoard = new ChessBoard();
            chessBoard.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT + TIP_BAR_HEIGHT));
            mainPanel.add(chessBoard, BorderLayout.CENTER);

            frame.add(mainPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    /**
     * 棋子类型枚举
     */
    enum PieceType {
        JIANG("将"), SHUAI("帅"), // 将/帅
        SHI("士"), // 士
        XIANG("象"), XIANG2("相"), // 象/相
        MA("马"), // 马
        CHE("车"), // 车
        PAO("炮"), // 炮
        BING("兵"), ZU("卒"); // 兵/卒

        private final String name;

        PieceType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 棋子颜色枚举
     */
    enum PieceColor {
        RED(new Color(200, 0, 0)), BLACK(new Color(0, 0, 0)); // 加深颜色更显眼

        private final Color color;

        PieceColor(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }

    /**
     * 棋子类（精准坐标计算）
     */
    static class ChessPiece {
        private final PieceType type;
        private final PieceColor color;
        private int gridX; // 格子列坐标（0-8）
        private int gridY; // 格子行坐标（0-9）
        private int originalGridX; // 拖动前的原始列坐标
        private int originalGridY; // 拖动前的原始行坐标
        private boolean isSelected; // 是否被选中

        public ChessPiece(PieceType type, PieceColor color, int gridX, int gridY) {
            this.type = type;
            this.color = color;
            this.gridX = gridX;
            this.gridY = gridY;
            this.originalGridX = gridX;
            this.originalGridY = gridY;
            this.isSelected = false;
        }

        // 获取棋子在面板上的实际像素X坐标（含扩展量，不含提示栏）
        public int getActualPixelX() {
            return EXTEND_SIZE + gridX * GRID_SIZE;
        }

        // 获取棋子在面板上的实际像素Y坐标（含扩展量，不含提示栏）
        public int getActualPixelY() {
            return EXTEND_SIZE + gridY * GRID_SIZE;
        }

        // 检查鼠标是否点击到该棋子（精准判定）
        public boolean isClicked(int mouseX, int mouseY) {
            // 鼠标Y坐标需要扣除提示栏高度
            int adjustedMouseY = mouseY - TIP_BAR_HEIGHT;
            int pieceX = getActualPixelX();
            int pieceY = getActualPixelY();

            // 圆形判定（棋子直径为GRID_SIZE）
            int dx = mouseX - pieceX;
            int dy = adjustedMouseY - pieceY;
            return dx * dx + dy * dy <= (GRID_SIZE / 2) * (GRID_SIZE / 2);
        }

        // 保存当前坐标为原始坐标
        public void saveOriginalPosition() {
            this.originalGridX = this.gridX;
            this.originalGridY = this.gridY;
        }

        // 回退到原始坐标
        public void revertToOriginalPosition() {
            this.gridX = this.originalGridX;
            this.gridY = this.originalGridY;
        }

        // Getter和Setter
        public PieceType getType() { return type; }
        public PieceColor getColor() { return color; }
        public int getGridX() { return gridX; }
        public void setGridX(int gridX) { this.gridX = gridX; }
        public int getGridY() { return gridY; }
        public void setGridY(int gridY) { this.gridY = gridY; }
        public boolean isSelected() { return isSelected; }
        public void setSelected(boolean selected) { isSelected = selected; }
    }

    /**
     * 棋盘面板类（精准鼠标交互）
     */
    static class ChessBoard extends JPanel {
        private final List<ChessPiece> pieces = new ArrayList<>();
        private ChessPiece selectedPiece = null;
        private int dragOffsetX; // 鼠标与棋子中心的X偏移（精准计算）
        private int dragOffsetY; // 鼠标与棋子中心的Y偏移（精准计算）
        private PieceColor currentTurn = PieceColor.RED; // 当前走棋方（红先）
        private boolean gameOver = false; // 游戏是否结束

        public ChessBoard() {
            initPieces();
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    handleMousePress(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    handleMouseRelease(e);
                }
            });
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    handleMouseDrag(e);
                }
            });
            setBackground(new Color(245, 222, 179)); // 仿木色背景
        }

        /**
         * 初始化棋子（使用格子坐标）
         */
        private void initPieces() {
            // 黑方棋子
            pieces.add(new ChessPiece(PieceType.CHE, PieceColor.BLACK, 0, 0));
            pieces.add(new ChessPiece(PieceType.MA, PieceColor.BLACK, 1, 0));
            pieces.add(new ChessPiece(PieceType.XIANG, PieceColor.BLACK, 2, 0));
            pieces.add(new ChessPiece(PieceType.SHI, PieceColor.BLACK, 3, 0));
            pieces.add(new ChessPiece(PieceType.JIANG, PieceColor.BLACK, 4, 0));
            pieces.add(new ChessPiece(PieceType.SHI, PieceColor.BLACK, 5, 0));
            pieces.add(new ChessPiece(PieceType.XIANG, PieceColor.BLACK, 6, 0));
            pieces.add(new ChessPiece(PieceType.MA, PieceColor.BLACK, 7, 0));
            pieces.add(new ChessPiece(PieceType.CHE, PieceColor.BLACK, 8, 0));
            pieces.add(new ChessPiece(PieceType.PAO, PieceColor.BLACK, 1, 2));
            pieces.add(new ChessPiece(PieceType.PAO, PieceColor.BLACK, 7, 2));
            pieces.add(new ChessPiece(PieceType.ZU, PieceColor.BLACK, 0, 3));
            pieces.add(new ChessPiece(PieceType.ZU, PieceColor.BLACK, 2, 3));
            pieces.add(new ChessPiece(PieceType.ZU, PieceColor.BLACK, 4, 3));
            pieces.add(new ChessPiece(PieceType.ZU, PieceColor.BLACK, 6, 3));
            pieces.add(new ChessPiece(PieceType.ZU, PieceColor.BLACK, 8, 3));

            // 红方棋子
            pieces.add(new ChessPiece(PieceType.CHE, PieceColor.RED, 0, 9));
            pieces.add(new ChessPiece(PieceType.MA, PieceColor.RED, 1, 9));
            pieces.add(new ChessPiece(PieceType.XIANG2, PieceColor.RED, 2, 9));
            pieces.add(new ChessPiece(PieceType.SHI, PieceColor.RED, 3, 9));
            pieces.add(new ChessPiece(PieceType.SHUAI, PieceColor.RED, 4, 9));
            pieces.add(new ChessPiece(PieceType.SHI, PieceColor.RED, 5, 9));
            pieces.add(new ChessPiece(PieceType.XIANG2, PieceColor.RED, 6, 9));
            pieces.add(new ChessPiece(PieceType.MA, PieceColor.RED, 7, 9));
            pieces.add(new ChessPiece(PieceType.CHE, PieceColor.RED, 8, 9));
            pieces.add(new ChessPiece(PieceType.PAO, PieceColor.RED, 1, 7));
            pieces.add(new ChessPiece(PieceType.PAO, PieceColor.RED, 7, 7));
            pieces.add(new ChessPiece(PieceType.BING, PieceColor.RED, 0, 6));
            pieces.add(new ChessPiece(PieceType.BING, PieceColor.RED, 2, 6));
            pieces.add(new ChessPiece(PieceType.BING, PieceColor.RED, 4, 6));
            pieces.add(new ChessPiece(PieceType.BING, PieceColor.RED, 6, 6));
            pieces.add(new ChessPiece(PieceType.BING, PieceColor.RED, 8, 6));
        }

        /**
         * 鼠标按下：精准选中棋子
         */
        private void handleMousePress(MouseEvent e) {
            if (gameOver) return;

            int mouseX = e.getX();
            int mouseY = e.getY();

            // 取消之前选中的棋子
            if (selectedPiece != null) {
                selectedPiece.setSelected(false);
                selectedPiece = null;
            }

            // 精准查找被点击的棋子
            for (ChessPiece piece : pieces) {
                if (piece.isClicked(mouseX, mouseY) && piece.getColor() == currentTurn) {
                    selectedPiece = piece;
                    selectedPiece.setSelected(true);
                    selectedPiece.saveOriginalPosition();

                    // 精准计算拖动偏移（扣除提示栏高度）
                    dragOffsetX = mouseX - piece.getActualPixelX();
                    dragOffsetY = (mouseY - TIP_BAR_HEIGHT) - piece.getActualPixelY();

                    repaint();
                    break;
                }
            }
        }

        /**
         * 鼠标拖动：精准跟随
         */
        private void handleMouseDrag(MouseEvent e) {
            if (selectedPiece != null && !gameOver) {
                repaint();
            }
        }

        /**
         * 鼠标释放：精准落子
         */
        private void handleMouseRelease(MouseEvent e) {
            if (selectedPiece == null || gameOver) return;

            int mouseX = e.getX();
            int mouseY = e.getY();

            // 计算鼠标在棋盘区域的实际坐标（扣除扩展量和提示栏）
            int boardMouseX = mouseX - dragOffsetX - EXTEND_SIZE;
            int boardMouseY = (mouseY - TIP_BAR_HEIGHT) - dragOffsetY - EXTEND_SIZE;

            // 转换为格子坐标（四舍五入）
            int targetGridX = Math.round(boardMouseX / (float) GRID_SIZE);
            int targetGridY = Math.round(boardMouseY / (float) GRID_SIZE);

            // 限制在合法格子范围内（0-8列，0-9行）
            targetGridX = Math.max(0, Math.min(COLUMNS - 1, targetGridX));
            targetGridY = Math.max(0, Math.min(ROWS - 1, targetGridY));

            // 临时保存原始位置
            int origGridX = selectedPiece.getGridX();
            int origGridY = selectedPiece.getGridY();
            ChessPiece capturedPiece = getPieceAt(targetGridX, targetGridY);

            boolean moveSuccess = false;
            // 校验移动合法性
            if (isMoveLegal(selectedPiece, targetGridX, targetGridY)) {
                // 执行移动
                removeCapturedPiece(targetGridX, targetGridY);
                selectedPiece.setGridX(targetGridX);
                selectedPiece.setGridY(targetGridY);

                // 检查是否自将
                if (!isGeneralInCheck(currentTurn)) {
                    moveSuccess = true;
                    // 切换走棋方
                    currentTurn = (currentTurn == PieceColor.RED) ? PieceColor.BLACK : PieceColor.RED;
                    // 检查将死
                    if (isCheckmate(currentTurn)) {
                        gameOver = true;
                        showLargeGameOverDialog((currentTurn == PieceColor.RED) ? "黑方" : "红方");
                    }
                } else {
                    // 自将，回滚
                    selectedPiece.setGridX(origGridX);
                    selectedPiece.setGridY(origGridY);
                    if (capturedPiece != null) {
                        pieces.add(capturedPiece);
                    }
                }
            } else {
                // 非法移动，回滚
                selectedPiece.revertToOriginalPosition();
            }

            // 取消选中
            selectedPiece.setSelected(false);
            selectedPiece = null;
            repaint();
        }

        /**
         * 显示放大的游戏结束弹窗
         */
        private void showLargeGameOverDialog(String winner) {
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "游戏结束", Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setSize(400, 250);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout());

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

            JLabel winnerLabel = new JLabel(winner + "获胜！");
            winnerLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
            winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            winnerLabel.setForeground(new Color(139, 69, 19));

            JLabel descLabel = new JLabel("对方将/帅被将死");
            descLabel.setFont(new Font("微软雅黑", Font.PLAIN, 20));
            descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            descLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

            JButton okButton = new JButton("确定");
            okButton.setFont(new Font("微软雅黑", Font.PLAIN, 18));
            okButton.setPreferredSize(new Dimension(120, 40));
            okButton.addActionListener(e -> dialog.dispose());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(okButton);

            contentPanel.add(winnerLabel);
            contentPanel.add(descLabel);
            dialog.add(contentPanel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            dialog.setVisible(true);
        }

        // ---------------- 核心规则校验（适配格子坐标） ----------------
        /**
         * 判断棋子移动是否合法
         */
        private boolean isMoveLegal(ChessPiece piece, int targetGridX, int targetGridY) {
            // 目标位置与原位置相同
            if (piece.getGridX() == targetGridX && piece.getGridY() == targetGridY) return false;
            // 目标位置有己方棋子
            if (hasAllyPiece(targetGridX, targetGridY, piece.getColor())) return false;

            // 按棋子类型校验
            switch (piece.getType()) {
                case JIANG:
                case SHUAI:
                    return checkJiangShuaiRule(piece, targetGridX, targetGridY);
                case SHI:
                    return checkShiRule(piece, targetGridX, targetGridY);
                case XIANG:
                case XIANG2:
                    return checkXiangRule(piece, targetGridX, targetGridY);
                case MA:
                    return checkMaRule(piece, targetGridX, targetGridY);
                case CHE:
                    return checkCheRule(piece, targetGridX, targetGridY);
                case PAO:
                    return checkPaoRule(piece, targetGridX, targetGridY);
                case BING:
                case ZU:
                    return checkBingZuRule(piece, targetGridX, targetGridY);
                default:
                    return false;
            }
        }

        /**
         * 校验将/帅规则
         */
        private boolean checkJiangShuaiRule(ChessPiece piece, int targetGridX, int targetGridY) {
            int xDiff = Math.abs(targetGridX - piece.getGridX());
            int yDiff = Math.abs(targetGridY - piece.getGridY());

            // 只能横竖走一步
            if (!((xDiff == 1 && yDiff == 0) || (xDiff == 0 && yDiff == 1))) return false;

            // 九宫格限制
            boolean isBlackJiang = piece.getColor() == PieceColor.BLACK;
            if (isBlackJiang) {
                if (targetGridY > 2 || targetGridX < 3 || targetGridX > 5) return false;
            } else {
                if (targetGridY < 7 || targetGridX < 3 || targetGridX > 5) return false;
            }

            // 不能与对方将/帅见面
            if (xDiff == 0) {
                ChessPiece enemyGeneral = findEnemyGeneral(piece.getColor());
                if (enemyGeneral != null && enemyGeneral.getGridX() == targetGridX) {
                    if (!isPathBlocked(piece.getGridX(), piece.getGridY(), enemyGeneral.getGridX(), enemyGeneral.getGridY())) {
                        return false;
                    }
                }
            }

            return true;
        }

        /**
         * 校验士规则
         */
        private boolean checkShiRule(ChessPiece piece, int targetGridX, int targetGridY) {
            int xDiff = Math.abs(targetGridX - piece.getGridX());
            int yDiff = Math.abs(targetGridY - piece.getGridY());

            // 只能斜线一步
            if (xDiff != 1 || yDiff != 1) return false;

            // 九宫格限制
            boolean isBlackShi = piece.getColor() == PieceColor.BLACK;
            if (isBlackShi) {
                if (targetGridY > 2 || targetGridX < 3 || targetGridX > 5) return false;
            } else {
                if (targetGridY < 7 || targetGridX < 3 || targetGridX > 5) return false;
            }

            return true;
        }

        /**
         * 校验象/相规则
         */
        private boolean checkXiangRule(ChessPiece piece, int targetGridX, int targetGridY) {
            int xDiff = Math.abs(targetGridX - piece.getGridX());
            int yDiff = Math.abs(targetGridY - piece.getGridY());

            // 走田字
            if (xDiff != 2 || yDiff != 2) return false;

            // 象眼无棋子
            int eyeX = (piece.getGridX() + targetGridX) / 2;
            int eyeY = (piece.getGridY() + targetGridY) / 2;
            if (hasAnyPiece(eyeX, eyeY)) return false;

            // 不过河
            boolean isBlackXiang = piece.getColor() == PieceColor.BLACK;
            if (isBlackXiang && targetGridY > 4) return false;
            if (!isBlackXiang && targetGridY < 5) return false;

            return true;
        }

        /**
         * 校验马规则
         */
        private boolean checkMaRule(ChessPiece piece, int targetGridX, int targetGridY) {
            int xDiff = Math.abs(targetGridX - piece.getGridX());
            int yDiff = Math.abs(targetGridY - piece.getGridY());

            // 走日字
            if (!((xDiff == 1 && yDiff == 2) || (xDiff == 2 && yDiff == 1))) return false;

            // 马腿无棋子
            int legX = piece.getGridX();
            int legY = piece.getGridY();
            if (xDiff == 2) {
                legX = piece.getGridX() + (targetGridX - piece.getGridX()) / 2;
            } else {
                legY = piece.getGridY() + (targetGridY - piece.getGridY()) / 2;
            }
            if (hasAnyPiece(legX, legY)) return false;

            return true;
        }

        /**
         * 校验车规则
         */
        private boolean checkCheRule(ChessPiece piece, int targetGridX, int targetGridY) {
            int xDiff = Math.abs(targetGridX - piece.getGridX());
            int yDiff = Math.abs(targetGridY - piece.getGridY());

            // 只能横竖走
            if (xDiff != 0 && yDiff != 0) return false;

            // 路径无阻隔
            if (isPathBlocked(piece.getGridX(), piece.getGridY(), targetGridX, targetGridY)) return false;

            return true;
        }

        /**
         * 校验炮规则
         */
        private boolean checkPaoRule(ChessPiece piece, int targetGridX, int targetGridY) {
            int xDiff = Math.abs(targetGridX - piece.getGridX());
            int yDiff = Math.abs(targetGridY - piece.getGridY());

            // 只能横竖走
            if (xDiff != 0 && yDiff != 0) return false;

            // 统计路径棋子数
            int pieceCount = countPiecesInPath(piece.getGridX(), piece.getGridY(), targetGridX, targetGridY);
            boolean hasEnemy = hasEnemyPiece(targetGridX, targetGridY, piece.getColor());

            // 移动：路径无棋子
            if (!hasEnemy) return pieceCount == 0;
                // 吃子：路径有且仅有一个炮架
            else return pieceCount == 1;
        }

        /**
         * 校验兵/卒规则
         */
        private boolean checkBingZuRule(ChessPiece piece, int targetGridX, int targetGridY) {
            int xDiff = Math.abs(targetGridX - piece.getGridX());
            int yDiff = Math.abs(targetGridY - piece.getGridY());

            // 只能走一步
            if (!((xDiff == 0 && yDiff == 1) || (xDiff == 1 && yDiff == 0))) return false;

            // 不能后退
            boolean isBlackZu = piece.getColor() == PieceColor.BLACK;
            if (isBlackZu) {
                if (targetGridY < piece.getGridY()) return false;
            } else {
                if (targetGridY > piece.getGridY()) return false;
            }

            // 过河前只能向前
            boolean isCrossRiver = isBlackZu ? (piece.getGridY() < 5) : (piece.getGridY() > 4);
            if (!isCrossRiver && xDiff != 0) return false;

            return true;
        }

        // ---------------- 胜负判定核心方法（适配格子坐标） ----------------
        /**
         * 检查指定方的将/帅是否被将军
         */
        private boolean isGeneralInCheck(PieceColor color) {
            ChessPiece general = findGeneral(color);
            if (general == null) return false;

            // 检查对方所有棋子是否能攻击到将/帅
            for (ChessPiece attacker : pieces) {
                if (attacker.getColor() != color && isMoveLegal(attacker, general.getGridX(), general.getGridY())) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 检查指定方是否被将死
         */
        private boolean isCheckmate(PieceColor color) {
            // 1. 先检查是否被将军
            if (!isGeneralInCheck(color)) return false;

            // 2. 尝试所有可能的走法
            for (ChessPiece piece : pieces) {
                if (piece.getColor() != color) continue;

                // 遍历所有可能的落点
                for (int x = 0; x < COLUMNS; x++) {
                    for (int y = 0; y < ROWS; y++) {
                        if (isMoveLegal(piece, x, y)) {
                            // 临时移动棋子
                            int origX = piece.getGridX();
                            int origY = piece.getGridY();
                            ChessPiece captured = getPieceAt(x, y);
                            removeCapturedPiece(x, y);
                            piece.setGridX(x);
                            piece.setGridY(y);

                            // 检查是否解除将军
                            boolean checkRemoved = !isGeneralInCheck(color);

                            // 回滚
                            piece.setGridX(origX);
                            piece.setGridY(origY);
                            if (captured != null) pieces.add(captured);

                            // 找到解将方法
                            if (checkRemoved) return false;
                        }
                    }
                }
            }

            // 无解将方法，将死
            return true;
        }

        // ---------------- 辅助方法（适配格子坐标） ----------------
        /**
         * 获取指定格子位置的棋子
         */
        private ChessPiece getPieceAt(int gridX, int gridY) {
            for (ChessPiece p : pieces) {
                if (p.getGridX() == gridX && p.getGridY() == gridY) {
                    return p;
                }
            }
            return null;
        }

        /**
         * 查找指定方的将/帅
         */
        private ChessPiece findGeneral(PieceColor color) {
            for (ChessPiece p : pieces) {
                if ((color == PieceColor.BLACK && p.getType() == PieceType.JIANG) ||
                        (color == PieceColor.RED && p.getType() == PieceType.SHUAI)) {
                    return p;
                }
            }
            return null;
        }

        /**
         * 查找对方的将/帅
         */
        private ChessPiece findEnemyGeneral(PieceColor color) {
            return findGeneral((color == PieceColor.RED) ? PieceColor.BLACK : PieceColor.RED);
        }

        /**
         * 检查目标位置是否有己方棋子
         */
        private boolean hasAllyPiece(int gridX, int gridY, PieceColor color) {
            ChessPiece piece = getPieceAt(gridX, gridY);
            return piece != null && piece.getColor() == color;
        }

        /**
         * 检查目标位置是否有对方棋子
         */
        private boolean hasEnemyPiece(int gridX, int gridY, PieceColor color) {
            ChessPiece piece = getPieceAt(gridX, gridY);
            return piece != null && piece.getColor() != color;
        }

        /**
         * 检查目标位置是否有任意棋子
         */
        private boolean hasAnyPiece(int gridX, int gridY) {
            return getPieceAt(gridX, gridY) != null;
        }

        /**
         * 移除目标位置的对方棋子
         */
        private void removeCapturedPiece(int gridX, int gridY) {
            pieces.removeIf(p -> p.getGridX() == gridX && p.getGridY() == gridY);
        }

        /**
         * 检查两点之间的路径是否有棋子阻隔
         */
        private boolean isPathBlocked(int x1, int y1, int x2, int y2) {
            return countPiecesInPath(x1, y1, x2, y2) > 0;
        }

        /**
         * 统计两点之间路径上的棋子数
         */
        private int countPiecesInPath(int x1, int y1, int x2, int y2) {
            int count = 0;
            if (y1 == y2) {
                // 横向
                int startX = Math.min(x1, x2) + 1;
                int endX = Math.max(x1, x2) - 1;
                for (int x = startX; x <= endX; x++) {
                    if (hasAnyPiece(x, y1)) count++;
                }
            } else if (x1 == x2) {
                // 纵向
                int startY = Math.min(y1, y2) + 1;
                int endY = Math.max(y1, y2) - 1;
                for (int y = startY; y <= endY; y++) {
                    if (hasAnyPiece(x1, y)) count++;
                }
            }
            return count;
        }

        // ---------------- 绘制方法（精准坐标） ----------------
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 1. 绘制走棋提示栏
            drawTurnTipBar(g2d);

            // 2. 绘制棋盘网格
            drawChessBoardGrid(g2d);

            // 3. 绘制楚河汉界文字
            drawRiverText(g2d);

            // 4. 绘制九宫格斜线
            drawNinePalace(g2d);

            // 5. 绘制棋子（精准坐标）
            drawChessPieces(g2d);
        }

        /**
         * 绘制走棋提示栏
         */
        private void drawTurnTipBar(Graphics2D g2d) {
            // 提示栏背景
            g2d.setColor(new Color(230, 230, 230));
            g2d.fillRect(0, 0, PANEL_WIDTH, TIP_BAR_HEIGHT);

            // 提示文字
            String turnText = currentTurn == PieceColor.RED ? "当前走棋：红方" : "当前走棋：黑方";
            g2d.setFont(new Font("微软雅黑", Font.BOLD, 18));
            g2d.setColor(currentTurn.getColor());

            // 文字居中
            FontMetrics fm = g2d.getFontMetrics();
            int textX = (PANEL_WIDTH - fm.stringWidth(turnText)) / 2;
            int textY = TIP_BAR_HEIGHT / 2 + fm.getAscent() / 2;

            g2d.drawString(turnText, textX, textY);

            // 提示栏边框
            g2d.setColor(Color.GRAY);
            g2d.drawRect(0, 0, PANEL_WIDTH - 1, TIP_BAR_HEIGHT - 1);
        }

        /**
         * 绘制棋盘网格（精准坐标）
         */
        private void drawChessBoardGrid(Graphics2D g2d) {
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1.5f));

            // 横线：0-9行，Y轴 = 扩展量 + 行号*格子大小 + 提示栏高度
            for (int y = 0; y < ROWS; y++) {
                int pixelY = EXTEND_SIZE + y * GRID_SIZE + TIP_BAR_HEIGHT;
                g2d.drawLine(EXTEND_SIZE, pixelY, EXTEND_SIZE + ORIG_BOARD_WIDTH, pixelY);
            }

            // 竖线：0-8列，X轴 = 扩展量 + 列号*格子大小
            for (int x = 0; x < COLUMNS; x++) {
                int pixelX = EXTEND_SIZE + x * GRID_SIZE;
                g2d.drawLine(pixelX, EXTEND_SIZE + TIP_BAR_HEIGHT,
                        pixelX, EXTEND_SIZE + ORIG_BOARD_HEIGHT + TIP_BAR_HEIGHT);
            }
        }

        /**
         * 绘制楚河汉界文字
         */
        private void drawRiverText(Graphics2D g2d) {
            g2d.setFont(new Font("宋体", Font.BOLD, 24));
            g2d.setColor(new Color(139, 69, 19));

            // 文字位置：4、5行之间
            int riverY = EXTEND_SIZE + (4 * GRID_SIZE + 5 * GRID_SIZE) / 2 + TIP_BAR_HEIGHT + 8;
            g2d.drawString("楚 河", EXTEND_SIZE + GRID_SIZE * 1, riverY);
            g2d.drawString("汉 界", EXTEND_SIZE + GRID_SIZE * 6, riverY);
        }

        /**
         * 绘制九宫格斜线
         */
        private void drawNinePalace(Graphics2D g2d) {
            int offset = EXTEND_SIZE + TIP_BAR_HEIGHT;

            // 黑方九宫
            g2d.drawLine(EXTEND_SIZE + 3 * GRID_SIZE, 0 * GRID_SIZE + offset,
                    EXTEND_SIZE + 5 * GRID_SIZE, 2 * GRID_SIZE + offset);
            g2d.drawLine(EXTEND_SIZE + 5 * GRID_SIZE, 0 * GRID_SIZE + offset,
                    EXTEND_SIZE + 3 * GRID_SIZE, 2 * GRID_SIZE + offset);

            // 红方九宫
            g2d.drawLine(EXTEND_SIZE + 3 * GRID_SIZE, 7 * GRID_SIZE + offset,
                    EXTEND_SIZE + 5 * GRID_SIZE, 9 * GRID_SIZE + offset);
            g2d.drawLine(EXTEND_SIZE + 5 * GRID_SIZE, 7 * GRID_SIZE + offset,
                    EXTEND_SIZE + 3 * GRID_SIZE, 9 * GRID_SIZE + offset);
        }

        /**
         * 绘制棋子（精准坐标，选中的棋子跟随鼠标）
         */
        private void drawChessPieces(Graphics2D g2d) {
            for (ChessPiece piece : pieces) {
                int px, py;

                if (piece.isSelected()) {
                    // 选中的棋子跟随鼠标
                    Point mousePos = MouseInfo.getPointerInfo().getLocation();
                    Point panelPos = this.getLocationOnScreen();

                    px = mousePos.x - panelPos.x - dragOffsetX;
                    py = mousePos.y - panelPos.y - dragOffsetY;
                } else {
                    // 未选中的棋子在固定位置
                    px = piece.getActualPixelX();
                    py = piece.getActualPixelY() + TIP_BAR_HEIGHT;
                }

                // 绘制棋子背景
                Color bgColor = piece.getColor() == PieceColor.RED
                        ? new Color(255, 220, 220)
                        : new Color(220, 220, 220);
                g2d.setColor(bgColor);
                g2d.fillOval(px - GRID_SIZE / 2, py - GRID_SIZE / 2, GRID_SIZE, GRID_SIZE);

                // 绘制棋子边框
                g2d.setColor(piece.getColor().getColor());
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawOval(px - GRID_SIZE / 2, py - GRID_SIZE / 2, GRID_SIZE, GRID_SIZE);

                // 绘制棋子文字
                g2d.setFont(new Font("宋体", Font.BOLD, 22));
                FontMetrics fm = g2d.getFontMetrics();
                String text = piece.getType().getName();
                int textX = px - fm.stringWidth(text) / 2;
                int textY = py + fm.getAscent() / 2 - 2;
                g2d.drawString(text, textX, textY);
            }
        }
    }
}