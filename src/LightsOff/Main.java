package LightsOff;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.Random;

public class Main extends JFrame {
    static int iconPreferSize = 30;
    /*
    图片路径推荐使用：getClass().getResource(图片路径);

    public URL getResource(String name)
    查找带有给定名称的资源。查找与给定类相关的资源的规则是通过定义类的 class loader 实现的。此方法委托给此对象的类加载器。如果此对象通过引导类加载器加载，则此方法将委托给 ClassLoader.getSystemResource(java.lang.String)。
    在委托前，使用下面的算法从给定的资源名构造一个绝对资源名：

    如果 name 以 '/' ('\u002f') 开始，则绝对资源名是'/' 后面的name 的一部分。
    否则，绝对名具有以下形式：
        modified_package_name/name
    其中 modified_package_name 是此对象的包名，该名用 '/' 取代了 '.' ('\u002e')。


    参数：
        name - 所需资源的名称
    返回：
        一个 URL 对象；如果找不到带有该名称的资源，则返回null
    ————————————————
    版权声明：本文为CSDN博主「披Zhe羊皮De狼」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
    原文链接：https://blog.csdn.net/u010995220/article/details/51176088
     */
    static ImageIcon iconLogo = new ImageIcon(Main.class.getResource("/resources/logo.png"));
    static Icon iconSmallLogo = new ImageIcon(Main.class.getResource("/resources/smallLogo.png"));
    static Icon iconMenu = new ImageIcon(Main.class.getResource("/resources/menu.png"));
    static Icon iconMenu2 = new ImageIcon(Main.class.getResource("/resources/menu2.png"));
    static Icon iconSuccess = new ImageIcon(Main.class.getResource("/resources/funny.png"));
    static Icon iconNew = new ImageIcon(Main.class.getResource("/resources/new.png"));
    static Icon iconNew2 = new ImageIcon(Main.class.getResource("/resources/new2.png"));
    static Icon iconReplay = new ImageIcon(Main.class.getResource("/resources/replay.png"));
    static Icon iconReplay2 = new ImageIcon(Main.class.getResource("/resources/replay2.png"));
    static Icon iconAdd = new ImageIcon(Main.class.getResource("/resources/add.png"));
    static Icon iconAdd2 = new ImageIcon(Main.class.getResource("/resources/add2.png"));
    static Icon iconMinus = new ImageIcon(Main.class.getResource("/resources/minus.png"));
    static Icon iconMinus2 = new ImageIcon(Main.class.getResource("/resources/minus2.png"));
    static Icon iconShot = new ImageIcon(Main.class.getResource("/resources/shot.png"));

    //浅黄：0xF6FD8B，浅蓝：BFE3ED
    static Color defaultOnC1 = new Color(246,253,139), defaultOnC2 = new Color(87, 85, 0),
            defaultOffC = Color.darkGray, defaultFgC = Color.WHITE, defaultBgC = Color.BLACK;
    //Block开着时，Block的颜色为OnC，OffC同理
    //when the Block is on, set the color to OnC, off to OffC.
    static Color OnC1 = defaultOnC1, OnC2 = defaultOnC2, OffC = defaultOffC,
            FgC = defaultFgC, BgC = defaultBgC;
    static Color OnC_darker, OnC2_darker, OffC_brighter;

    static final int defaultSize = 5, defaultMaxSize = 10, defaultMinSize = 2;
    int rowSize = defaultSize, coloumnSize = defaultSize;
    protected enum LevelMode{
        Random, Fixed, Increase
    }
    LevelMode levelMode = LevelMode.Increase;
    int level = 1;

    static MainPanel mainPanel;
    Block[][] blocks = null;
    //当前的地图
    boolean[][] tempMap = null;

    JDialog jd = null;

    static Font defaultfont = new Font("Times New Roman" , Font.BOLD , 16 );

    //创建菜单
    JMenuBar jmb = new JMenuBar();
    JMenu jm = new JMenu();
    //选择游戏模式
    JMenu jmMode = new JMenu("Mode(选择模式)");
    JRadioButtonMenuItem jbIncreaseMode = new JRadioButtonMenuItem("Increase Level(等级递增)", true);
    JRadioButtonMenuItem jbFixedMode = new JRadioButtonMenuItem("Fixed Level(固定等级)", false);
    JRadioButtonMenuItem jbRandomMode = new JRadioButtonMenuItem("Random Level(随机等级)", false);
    //设置行与列数
    JMenu jmSize = new JMenu("Size(大小)");
    //设置前景色
    JMenu jmFgC = new JMenu("Fg Color(前景色)");
    JMenuItem FgOther = new JMenuItem("single color(单色)");
    JMenuItem FgOther2 = new JMenuItem("gradient color(渐变色)");
    //设置背景色
    JMenu jmBgC = new JMenu("Bg Color(背景色)");
    JMenuItem BgOther = new JMenuItem("Other(其他)");
    //设置主题颜色：字体颜色
    JMenu jmThemeFgC = new JMenu("Font color(字体颜色)");
    //设置主题颜色：背景颜色
    JMenu jmThemeBgC = new JMenu("Theme Bg color(主题背景颜色)");


    //工具栏
    JToolBar jtb = new JToolBar();
    JButton jbNew = new JButton(iconNew);
    JButton jbReplay = new JButton(iconReplay);
    JLabel jlLevel = new JLabel("Level:");
    JButton jbLevelUp = new JButton(iconAdd);
    JButton jbLevelDown = new JButton(iconMinus);
    //关于Lights Off
    JButton jbAbout = new JButton(iconSmallLogo);

    public Main(){
        setLevel(1);

        setLayout(new BorderLayout());

        //将jm整个赋给jmb，把jmb整个加到工具栏
        jtb.add(jmb);

        jmb.add(jm);

        jm.setBorderPainted(false);
        jm.setIcon(iconMenu);
        jm.setToolTipText("Menu/菜单");
        jm.setPreferredSize(new Dimension(iconPreferSize, iconPreferSize));
        jm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                jm.setIcon(iconMenu2);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                jm.setIcon(iconMenu);
            }
        });

        //选择模式 菜单
        jm.add(jmMode);

        jbIncreaseMode.addActionListener(actionEvent -> {
            levelMode = LevelMode.Increase;
            setLevel(1);    //从1开始
            jbLevelUp.setEnabled(true);
            jbLevelDown.setEnabled(true);
        });

        jbFixedMode.addActionListener(actionEvent -> {
            levelMode = LevelMode.Fixed;
            setLevel(1);
            jbLevelUp.setEnabled(true);
            jbLevelDown.setEnabled(true);
        });

        jbRandomMode.addActionListener(actionEvent -> {
            levelMode = LevelMode.Random;
            jlLevel.setText("Level:Random");
            jbLevelUp.setEnabled(false);
            jbLevelDown.setEnabled(false);
        });

        jmMode.add(jbIncreaseMode);
        jmMode.add(jbFixedMode);
        jmMode.add(jbRandomMode);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(jbIncreaseMode);
        buttonGroup.add(jbFixedMode);
        buttonGroup.add(jbRandomMode);

        //大小更改菜单
        jm.add(jmSize);
        //jMenu 不添加item 直接自己添加监听
        //actionPerformed不行，mousePressed可以，见鬼了
        jmSize.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                (new SizeChooser()).showSizeChooser();
            }
        });

        //前景色菜单
        jm.add(jmFgC);
        //菜单下拉项
        jmFgC.add(new FgJMI(defaultOnC1, defaultOnC2));
        jmFgC.add(new FgJMI(new Color(191,227,237), new Color(0, 6, 255))); //浅蓝
        jmFgC.add(new FgJMI(new Color(153,255,102), new Color(0,107,15)));     //浅绿
        jmFgC.add(new FgJMI(new Color(255,169,255), new Color(51,0,51)));     //浅紫
        jmFgC.add(new FgJMI(Color.PINK, Color.PINK.darker().darker()));
        jmFgC.add(new FgJMI(Color.WHITE, Color.darkGray));
        jmFgC.addSeparator();

        //设置单色
        FgOther.addActionListener(actionEvent -> {
            //存储原有颜色
            Color staticColor = OnC1, staticColor2 = OnC2;
            JColorChooser jcc = new JColorChooser(staticColor);
            jcc.setDragEnabled(true);
            jcc.getSelectionModel().addChangeListener(changeEvent -> setOnC(jcc.getColor(),jcc.getColor()));
            JColorChooser.createDialog(null, "Choose Foreground Color:Single Color/设置前景色：单色",
                    false, jcc, null,
                    actionEvent1 -> setOnC(staticColor, staticColor2)).setVisible(true);
        });
        jmFgC.add(FgOther);

        //设置渐变色
        FgOther2.addActionListener(actionEvent -> {
            //存储原有颜色
            Color staticColor1 = OnC1, staticColor2 = OnC2;
            JColorChooser jcc1 = new JColorChooser(staticColor1), jcc2 = new JColorChooser(staticColor2);
            ChangeListener listener = changeEvent -> {
                setOnC(jcc1.getColor(), jcc2.getColor());
                System.out.println("Color1:" + jcc1.getColor());
                System.out.println("Color2:" + jcc2.getColor());
            };
            jcc1.getSelectionModel().addChangeListener(listener);
            jcc2.getSelectionModel().addChangeListener(listener);

            JColorChooser.createDialog(mainPanel, "Choose Foreground Color:Gradient Color(Top Left Corner)/设置前景色：渐变色(左上角)",
                    false, jcc1, null, actionEvent1 -> setOnC(staticColor1, OnC2)).setVisible(true);
            JColorChooser.createDialog(mainPanel, "Choose Foreground Color:Gradient Color(Bottom Right Corner)/设置前景色：渐变色(右下角)",
                    false, jcc2, null, actionEvent1 -> setOnC(OnC1, staticColor2)).setVisible(true);

        });
        jmFgC.add(FgOther2);

        //设置背景色菜单
        jm.add(jmBgC);
        jmBgC.add(new BgJMI(defaultOffC));
        jmBgC.add(new BgJMI(Color.decode("0x330000")));
        jmBgC.add(new BgJMI(Color.decode("0x000033")));
        jmBgC.add(new BgJMI(Color.decode("0x330033")));
        jmBgC.addSeparator();

        BgOther.addActionListener(actionEvent -> {
            //存储原有颜色
            Color staticColor = OffC;
            JColorChooser jcc = new JColorChooser(staticColor);
            jcc.setDragEnabled(true);
            jcc.getSelectionModel().addChangeListener(changeEvent -> setOffC(jcc.getColor()));
            JColorChooser.createDialog(null, "Choose Background Color/设置背景色",
                    false, jcc, null,
                    actionEvent1 -> setOffC(staticColor)).setVisible(true);
        });
        jmBgC.add(BgOther);

        jm.add(jmThemeFgC);
        jmThemeFgC.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //存储原有颜色
                Color staticColor = FgC;
                JColorChooser jcc = new JColorChooser(staticColor);
                jcc.setDragEnabled(true);
                jcc.getSelectionModel().addChangeListener(changeEvent -> setFgC(jcc.getColor()));
                JColorChooser.createDialog(null, "Choose Font Color/设置字体颜色",
                        false, jcc, null,
                        actionEvent1 -> setFgC(staticColor)).setVisible(true);
            }
        });

        //设置主题背景
        jm.add(jmThemeBgC);
        jmThemeBgC.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //存储原有颜色
                Color staticColor = BgC;
                JColorChooser jcc = new JColorChooser(staticColor);
                jcc.setDragEnabled(true);
                jcc.getSelectionModel().addChangeListener(changeEvent -> setBgC(jcc.getColor()));
                JColorChooser.createDialog(null, "Choose Theme Background Color/设置主题背景颜色",
                        false, jcc, null,
                        actionEvent1 -> setBgC(staticColor)).setVisible(true);
            }
        });



        //设置工具栏
        //“重玩”
        jbReplay.setPreferredSize(new Dimension(iconPreferSize, iconPreferSize));
        jbReplay.setToolTipText("Replay/重玩");
        jbReplay.setBorderPainted(false);
        jbReplay.setRolloverIcon(iconReplay2);
        jbReplay.addActionListener(actionEvent -> {
            for(int row = 0, column; row < rowSize; row++){
                for(column = 0; column < coloumnSize; column++){
                    if(tempMap[row][column] != blocks[row][column].isOn())
                        blocks[row][column].turn();
                }
            }
        });
        jtb.add(jbReplay);

        //“新关卡”
        jbNew.setPreferredSize(new Dimension(iconPreferSize, iconPreferSize));
        jbNew.setToolTipText("New Map/新关卡");
        jbNew.setBorderPainted(false);
        jbNew.setRolloverIcon(iconNew2);    //鼠标悬停时显示
        jbNew.addActionListener(actionEvent -> mainPanel.newMap(0));
        jtb.add(jbNew);

        //分隔
        jtb.addSeparator(new Dimension(iconPreferSize * 2, iconPreferSize));

        //“难度降低”
        jbLevelDown.setPreferredSize(new Dimension(iconPreferSize, iconPreferSize));
        jbLevelDown.setToolTipText("Level Down");
        jbLevelDown.setRolloverIcon(iconMinus2);
        jbLevelDown.setBorderPainted(false);
        jbLevelDown.addActionListener(actionEvent -> setLevel(level - 1));
        jtb.add(jbLevelDown);

        //分隔
        jtb.addSeparator(new Dimension(iconPreferSize, iconPreferSize));

        //显示难度
        jtb.add(jlLevel);
        jlLevel.setFont(defaultfont);
        jlLevel.setPreferredSize(new Dimension(iconPreferSize * 3, iconPreferSize));

        //分隔
        jtb.addSeparator(new Dimension(iconPreferSize, iconPreferSize));

        //“难度升高”
        jbLevelUp.setPreferredSize(new Dimension(iconPreferSize, iconPreferSize));
        jbLevelUp.setToolTipText("Level Up");
        jbLevelUp.setBorderPainted(false);
        jbLevelUp.addActionListener(actionEvent -> setLevel(level + 1));
        jbLevelUp.setRolloverIcon(iconAdd2);
        jtb.add(jbLevelUp);

        //分隔
        jtb.addSeparator(new Dimension(iconPreferSize * 2, iconPreferSize));

        //"关于Lights Off"
        jtb.add(jbAbout);
        jbAbout.setToolTipText("About <Lights Off>/关于<Lights Off>");
        jbAbout.setPreferredSize(new Dimension(iconPreferSize * 2, iconPreferSize));
        jbAbout.addActionListener(actionEvent ->  {
            Border lb = BorderFactory.createLineBorder(BgC, 10, true);
            JTextArea jta1, jta2;

            JPanel panelIntro = new JPanel(new BorderLayout());
            panelIntro.setBorder(BorderFactory.createTitledBorder(lb, "Introduction/介绍",
                    TitledBorder.LEADING, TitledBorder.TOP, null, BgC));
            panelIntro.add(jta1 = new JTextArea("<Lights Off> is a casual puzzle game.\n" +
                    "You need to turn Off ALL the lights of n rows and m columns in order to gain success.\n" +
                    "When you turn on/off a light, the lights around will also turn as a consequence.\n" +
                    "just like this:"), BorderLayout.NORTH);
            jta1.setFont(defaultfont);
            panelIntro.add(new JTextArea("《Lights Off》是一款休闲益智小游戏。\n" +
                    "你需要将n行m列的灯全部熄灭。\n" +
                    "当你开/关某一盏灯时，它周围的灯也会同时转换。\n" +
                    "例如："), BorderLayout.CENTER);
            panelIntro.add(new JLabel(iconShot), BorderLayout.SOUTH);

            JPanel panelAuthor = new JPanel();
            panelAuthor.setBorder(BorderFactory.createTitledBorder(lb, "Author/作者",
                    TitledBorder.LEADING, TitledBorder.TOP, null, BgC));
            panelAuthor.add(jta2 = new JTextArea("Jack Dondy, jackdondy9@gmail.com\n" +
                    "Inspire by \"https://wiki.gnome.org/Apps/Lightsoff\""));
            jta2.setFont(defaultfont);

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(panelIntro, BorderLayout.NORTH);
            panel.add(panelAuthor, BorderLayout.SOUTH);
            JOptionPane.showMessageDialog(mainPanel, panel, "About \"Lights Off\"",
                    JOptionPane.INFORMATION_MESSAGE, iconLogo);
        });

        jtb.setBorderPainted(false);
        jtb.setFloatable(false);
//        jtb.setPreferredSize(new Dimension(500, 50));
        add(jtb, BorderLayout.NORTH);

        add(mainPanel = new MainPanel(), BorderLayout.CENTER);

        setFgC(FgC);
        setBgC(BgC);
    }

    public static void main(String[] args){
        JFrame frame = new Main();
        frame.setIconImage(iconLogo.getImage());
        frame.setTitle("Lights Off");
        frame.setLocationRelativeTo(null);      //设置登陆框出现的位置，null表示出现在屏幕正中央
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        mainPanel.repaint();
    }
    //设置主题前景色（字体颜色）
    public void setFgC(Color color){
        FgC = color;
        setForeground(color);
        jtb.setForeground(color);
        jm.setForeground(color);
        jmSize.setForeground(color);
        jmMode.setForeground(color);
        jbIncreaseMode.setForeground(color);
        jbFixedMode.setForeground(color);
        jbRandomMode.setForeground(color);

        jmFgC.setForeground(color);
        FgOther.setForeground(color);
        FgOther2.setForeground(color);

        jmBgC.setForeground(color);
        BgOther.setForeground(color);

        jmThemeBgC.setForeground(color);
        jmThemeFgC.setForeground(color);

        jlLevel.setForeground(color);
    }

    //设置主题背景色
    public void setBgC(Color color){
        BgC = color;
        setBackground(color);

        jtb.setBackground(color);
        jm.setBackground(color);
        jmb.setBackground(color);

        jmSize.setBackground(color);

        jmMode.setBackground(color);
        jbIncreaseMode.setBackground(color);
        jbFixedMode.setBackground(color);
        jbRandomMode.setBackground(color);

        jmFgC.setBackground(color);
        FgOther.setBackground(color);
        FgOther2.setBackground(color);

        jmBgC.setBackground(color);
        BgOther.setBackground(color);

        jmThemeFgC.setBackground(color);
        jmThemeBgC.setBackground(color);

        jtb.setBackground(color);
        jbReplay.setBackground(color);
        jbNew.setBackground(color);
        jbLevelUp.setBackground(color);
        jbLevelDown.setBackground(color);
        jlLevel.setBackground(color);
        jbAbout.setBackground(color);
        mainPanel.setBackground(color);
    }

    //一个计时消失的对话框
    public void showTimeDialog(int sec, JFrame parent, String str){

    }

    //设置Block开状态下的颜色（渐变）
    public void setOnC(Color onC, Color onC2) {
        OnC1 = onC;
        OnC2 = onC2;
        OnC_darker = onC.darker();
        OnC2_darker = onC2.darker();
        for(int i = 0, j; i < rowSize; i++){
            for(j = 0; j < coloumnSize; j++){
                blocks[i][j].repaint();
            }
        }
    }

    //设置Block关状态下的颜色
    public void setOffC(Color offC) {
        OffC = offC;
        OffC_brighter = offC.brighter();
        for(int i = 0, j; i < rowSize; i++){
            for(j = 0; j < coloumnSize; j++){
                blocks[i][j].repaint();
            }
        }
    }

    protected class FgJMI extends JMenuItem{
        protected Color c1, c2;
        FgJMI(Color color1, Color color2){
            c1 = color1;
            c2 = color2;
            addMouseListener(new MouseAdapter(){
                //保存原来的颜色
                protected Color staticC1, staticC2;
                //mouseClicked似乎永远不会被调用，就算mouseEntered没有重写
//                public void mouseClicked(MouseEvent var1){
//                    System.out.println("Click");
//                    setOnC(thisColor);
//                }

                public void mouseEntered(MouseEvent var1){
//                    System.out.println("Enter");
                    //先保存旧颜色，以备恢复
                    staticC1 = OnC1;
                    staticC2 = OnC2;
                    setOnC(c1, c2);
//                    System.out.println(c1 + "and" + c2);
                }

                public void mouseExited(MouseEvent var1){
                    //恢复旧颜色
                    setOnC(staticC1, staticC2);
                }
            });
        }

        @Override
        public void paint(Graphics g){
            Graphics2D g2d = (Graphics2D) g.create();
            int h = getHeight();
            int w = getWidth();
            g2d.setPaint(new GradientPaint(0, 0, c1, w, h, c2, true));
            g2d.fillRect(0, 0, w, h);
        }
    }

    protected class BgJMI extends JMenuItem{
        protected Color thisColor;
        BgJMI(Color _thisColor){
            thisColor = _thisColor;
            setBackground(thisColor);
            addMouseListener(new MouseAdapter(){
                protected Color staticColor;

                public void mouseEntered(MouseEvent var1){
                    //先保存旧颜色，以备恢复
                    staticColor = OffC;
                    setOffC(thisColor);
                }

                public void mouseExited(MouseEvent var1){
                    //恢复旧颜色
                    setOffC(staticColor);
                }
            });
        }
    }

    //先判断该块是否存在，然后转变状态
    //ensure the Block is exists, then turn the Block.
    protected void SafeTurn(int _row, int _column){
        if(_column < 0 || _column > coloumnSize - 1)   return;
        if(_row < 0 || _row > rowSize - 1)     return;
        blocks[_row][_column].turn();
    }

    //当Mode不为Random时，调用该函数
    protected void setLevel(int newlevel){
        //level的范围是1到size，size取行和列的平均值
        //newlevel不合法
        if(newlevel < 1 || newlevel * 2 > rowSize + coloumnSize) return;
        else level = newlevel;

        jlLevel.setText("Level:" + level);
        System.out.println("Level:" + level);
        if(mainPanel == null)   return;
        mainPanel.newMap(0);
    }

    protected class MainPanel extends JPanel{
        MainPanel(){
            tempMap = new boolean[rowSize][coloumnSize];
            blocks = new Block[rowSize][coloumnSize];
            for(int i = 0; i < rowSize; i++){
                for(int j = 0; j < coloumnSize; j++){
                    blocks[i][j] = new Block();
                    blocks[i][j].addActionListener(new BlockListener(i, j));
                    add(blocks[i][j]);
                }
            }
            setLevel(1);
            newMap(0);
            setLayout(new GridLayout(rowSize, coloumnSize, 2, 2));
            setPreferredSize(new Dimension(coloumnSize * 80,rowSize * 80));
            setOnC(OnC1, OnC2);
            setOffC(OffC);
            repaint();
        }

        /*如果不重写repaint方法，而是设置ComponentListener来监听MainPanel的大小变化，实现componentResized方法，好像无法实现
            因为每次画图时，在设定panel大小前，布局管理器获取的panel大小是旧的。
            即，每次大小变化在触发componentResized方法前，都先用布局管理器先画好了(或者先repaint了)，并且使用的是旧大小。
         */
        //监听MainPanel的大小变化，并将内部的blocks调整为正方形
        @Override
        public void repaint(){
            int blockSize = Math.min(getHeight() / rowSize, getWidth() / coloumnSize);
//            setLocation();    //setLocation会报错显示栈溢出
//            setSize();
            setBounds(getX() + (getWidth() - blockSize * coloumnSize) / 2,
                    getY() + (getHeight() - blockSize * rowSize) / 2,
                    blockSize * coloumnSize, blockSize * rowSize);
        }

        //产生新的地图
        protected void newMap(int failTimes){
            //重试5次还不成功，则提示并返回
            if(failTimes > 4){
                JOptionPane.showMessageDialog(mainPanel, "Failed to generate a new Map!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //开始前，将所有灯关一遍
            for(int column, row = 0; row < rowSize; row++){
                for(column = 0; column < coloumnSize; column++){
                    blocks[row][column].turnOff();
                }
            }
            int steps = 0;
            Random random = new Random(System.currentTimeMillis());
            //首先，计算所需的最短步数
            switch (levelMode){
                case Increase:
                case Fixed:
                    /*
                    Level:  steps
                        1:  1
                        2:  2-3
                        3:  4-6
                        4:  7 to 10
                        5:  11 to 15
                        ...
                        n(n <= size):  n(n-1)/2 + 1  to n(n+1)/2
                     */
                    if(level < 0)   return;
                    steps = level * (level - 1) / 2 + 1 + random.nextInt(level);
                    break;
                case Random:
                    //随机步数：范围是 1 至 size * size
                    steps = 1 + random.nextInt(rowSize * coloumnSize);
                    break;
            }

            System.out.println("Steps:" + steps);
            //根据步数，转换若干次灯，保证相邻两步不会点到同一盏灯，否则难度降低
            int lastRow = -1, lastColumn = -1;
            for(int i, row, column; steps > 0; steps--){
                //转换第i盏灯，同时周围的灯转换状态
                i = random.nextInt(rowSize * coloumnSize) + 1;
                for(row = 0; row < rowSize; row ++){
                    for(column = 0; column < coloumnSize; column++){
                        i--;
                        if(i == 0){
                            if(row == lastRow && column == lastColumn){
                                steps++;//又选到了这盏灯，该步作废，步数加一，
                                break;
                            }
                            lastRow = row;
                            lastColumn = column;
                            blocks[row][column].turn();
                            SafeTurn(row - 1, column);
                            SafeTurn(row + 1, column);
                            SafeTurn(row, column + 1);
                            SafeTurn(row, column - 1);
                            break;
                        }
                    }
                    if(i == 0)      break;
                }
            }
            boolean isValid = false;
            //最后，将blocks记录到map，以便从头开始
            for(int row = 0, column; row < rowSize; row++){
                for(column = 0; column < coloumnSize; column++){
                    tempMap[row][column] = blocks[row][column].isOn();
                    if(blocks[row][column].isOn())  isValid = true;
                }
            }
            //如果产生的地图为空，重新产生(该情况可能发生)
            if(!isValid)    newMap(failTimes + 1);
        }

    }

    //该类监听一个Block
    //a BlockListener listens to the action of a Block
    protected class BlockListener implements ActionListener {
        //行和列代表Block的位置
        //row & column is the position of the Block
        protected int row, column;
        BlockListener(int _row, int _column){
            this.row = _row;
            this.column = _column;
        }

        //当该块被按下时，调用该函数
        //转换该块周围4个块的状
        //this func will be called when this Block is clicked.
        //turn this block and the 4 blocks around it
        public void actionPerformed(ActionEvent e) {
            blocks[row][column].turn();
            SafeTurn(row + 1, column);
            SafeTurn(row - 1, column);
            SafeTurn(row, column + 1);
            SafeTurn(row, column - 1);
            //判断是否可以过关
            for(int _column, _row = 0; _row < rowSize; _row ++) {
                for (_column = 0; _column < coloumnSize; _column++) {
                    //不可过关
                    if(blocks[_row][_column].isOn())  return;
                }
            }

            //可以过关
            JOptionPane.showMessageDialog(mainPanel," W i n\n老司机过关", "Tip/温馨提示",
                    JOptionPane.INFORMATION_MESSAGE, iconSuccess);

            switch (levelMode){
                case Increase:
                    level++;
                    if(level * 2 > rowSize + coloumnSize){
                        //此时弹出消息提示框，并自动按下 模式菜单
                        JOptionPane.showMessageDialog(mainPanel,"[Level Mode: Increase] accomplished", "",
                                JOptionPane.INFORMATION_MESSAGE, iconSuccess);
                        jmMode.doClick();
                        return;
                    }
                    setLevel(level);
                    break;
                case Fixed:
                    setLevel(level);
                    break;
                case Random:
                    mainPanel.newMap(0);
                    break;
            }

        }

    }

    //由SizeChooser调用，以设置新大小
    void setNewSize(int _rowSize, int _coloumnSize){
        rowSize = _rowSize;
        coloumnSize = _coloumnSize;
        if(levelMode != LevelMode.Random){
            level = 1;
        }
        remove(mainPanel);
        add(mainPanel = new MainPanel(), BorderLayout.CENTER);
        setFgC(FgC);
        setBgC(BgC);
        //事先设置子元素的preferSize，然后一起pack，
        //重写repaint方法手动repaint子元素 ，然后调用repaint方法好像没用
        pack();
    }

    protected class SizeChooser{
        JFrame newFrame = new JFrame("Setting Size");
        JPanel panel = new JPanel(new GridLayout(defaultMaxSize, defaultMaxSize, 2, 2));
        //如果是JLabel，好像MouseClicked不会被调用
        JButton[][] settingButtons = new JButton[defaultMaxSize][defaultMaxSize];
        int newRowSize, newColSize;

        TitledBorder tb;

        JButton jbOK = new JButton("OK/确定");

        //展示大小选择窗口，如果做了更改，直接改动rowSize和columnSize，并重新实例化MainPanel。
        void showSizeChooser(){
            newRowSize = rowSize;
            newColSize = coloumnSize;

            newFrame.setLocationRelativeTo(mainPanel);
//            newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //会把全部退出
            newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            newFrame.setLayout(new BorderLayout(30,30));
            newFrame.setVisible(true);

            panel.setBackground(BgC);
            panel.setPreferredSize(new Dimension(400, 400));

            panel.setBorder(tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BgC, 30),
                    "", TitledBorder.CENTER, TitledBorder.ABOVE_BOTTOM, defaultfont, FgC));

            for(int i = 0, j; i < defaultMaxSize; i++){
                for(j = 0; j < defaultMaxSize; j++){
                    settingButtons[i][j] = new JButton();

                    settingButtons[i][j].setForeground(BgC);
                    settingButtons[i][j].addMouseListener(new settingButtonListener(i, j));
                    panel.add(settingButtons[i][j]);
                }
            }
            MouseAdapter MA;
            panel.addMouseListener(MA = new MouseAdapter() {
                @Override
                //不知道有没有用
                public void mouseExited(MouseEvent e) {
                    //展示 newRowSize * newColSize
                    //即，点击settingButtons[newRowSize - 1][newColSize - 1]
                    //settingButtons[newRowSize - 1][newColSize - 1].doClick(); //这句不会激发自定义的鼠标监听器
                    settingButtons[newRowSize - 1][newColSize - 1].setText(newRowSize + " × " + newColSize);
                    tb.setTitle(newRowSize + " × " + newColSize);
                    panel.repaint();    //以应用title的更改
                    //左上方的label全部显示OnC,包括自己, 其他label显示OffC
                    for(int i = 0, j; i < defaultMaxSize; i++){
                        for(j = 0; j < defaultMaxSize; j++){
                            settingButtons[i][j].setBackground((i <= (newRowSize - 1) && j <= (newColSize - 1))? OnC1 : OffC);
                        }
                    }
                }
            });
            //初始化
            MA.mouseExited(null);

            newFrame.add(panel, BorderLayout.CENTER);

            jbOK.setPreferredSize(new Dimension(60, 30));
            jbOK.setForeground(FgC);
            jbOK.setBackground(BgC);
            jbOK.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    newFrame.dispose();
                    //设置主面板的新大小
                    setNewSize(newRowSize, newColSize);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    //重新画一遍设定好的大小，防止移动太快导致绘画略过
                    MA.mouseExited(null);
                }
            });
            newFrame.add(jbOK, BorderLayout.SOUTH);
            newFrame.pack();
        }

        protected class settingButtonListener extends MouseAdapter{
            int row, column;
            settingButtonListener(int _row, int _column){
                row = _row;
                column = _column;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                //Size太小时，不允许选择
                if(row + 1 < defaultMinSize && column + 1 < defaultMinSize)     return;
                newRowSize = row + 1;
                newColSize = column + 1;
                mouseEntered(e);
                System.out.println("Clicked!");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //左上方的label全部显示OnC,包括自己, 其他label显示OffC
                for(int i = 0, j; i < defaultMaxSize; i++){
                    for(j = 0; j < defaultMaxSize; j++){
                        settingButtons[i][j].setBackground((j <= column && i <= row)? OnC1 : OffC);
                        settingButtons[i][j].setText("");
                    }
                }
                //显示自己是几×几
                settingButtons[row][column].setText((row + 1) + " × " + (column + 1));
                //显示当前是几×几
                tb.setTitle((row + 1) + " × " + (column + 1));
                panel.repaint();
                System.out.println((row + 1) + " × " + (column + 1));
            }

        }

    }
}

/*
JFrame.pack()
pack() 调整此窗口的大小，以适合其子组件的首选大小和布局
单独使用setSize()时，不能使用pack()，否则按照pack()自动适配

单独使用pack()时，是按照组件的大小自动适配的

单独使用setPreferredSize()时，设置的大小无效，必须在后面添加pack()配合显示，否则设置效果不生效
————————————————
版权声明：本文为CSDN博主「sherlocksy」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/sherlocksy/article/details/80110773
 */