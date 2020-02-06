package LightsOff;

import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


enum BlockState{
    On, Off
}

//该类表示n*n面板上的一个块
//a Block indicates a block of the n * n panel
class Block extends JButton {
    //初始状态为 关
    //the initial state is off.
    protected BlockState state = BlockState.Off;
    //鼠标是否悬停在该按钮上
    boolean hover = false;

    Block(){
        addMouseListener(new innerWindowListener());
        setBorderPainted(false);
        setContentAreaFilled(false);
    }

    protected void turnOff(){
        state = BlockState.Off;
        repaint();
    }

    protected void turn(){
        state = ((state == BlockState.Off)? BlockState.On : BlockState.Off);
        repaint();
    }

    protected boolean isOn(){
        return (BlockState.On == state);
    }

    protected void paintComponent(Graphics g) {
        Color c1, c2;
        if(state == BlockState.On){
            if(hover){
                c1 = Main.OnC_darker;
                c2 = Main.OnC2_darker;
            }else{
                c1 = Main.OnC1;
                c2 = Main.OnC2;
            }
        }else{
            if(hover){
                c1 = c2 = Main.OffC_brighter;
            }else c1 = c2 = Main.OffC;
        }
        Graphics2D g2d = (Graphics2D) g.create();
        int h = getHeight();
        //圆角处的角度
        int arc = h / 8;
        Shape clip = g2d.getClip();
        //设置圆角矩形区域
        g2d.clip(new RoundRectangle2D.Float(0, 0, h, h, arc, arc));
        //绘制矩形渐变色
        g2d.setPaint(new GradientPaint(0, 0, c1, h, h, c2, true));
        g2d.fillRect(0, 0, h, h);

        //绘制外层边框
        g2d.setClip(clip);
        g2d.setPaint(new GradientPaint(0, h, new Color(0, 0, 0, 53),
                h, 0, new Color(255, 255, 255, 126)));
        g2d.drawRoundRect(1, 1, h - 2, h - 2, arc, arc);
        g2d.dispose();
        super.paintComponent(g);
    }

    @Override
    public void paint(Graphics g) {
        paintComponent(g);
        super.paint(g);
    }

    protected class innerWindowListener extends MouseAdapter{

        public void mouseEntered(MouseEvent e) {
            hover = true;
        }

        public void mouseExited(MouseEvent e) {
            hover = false;
        }
    }
}
