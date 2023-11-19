package space.haobin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.Random;

/**
 * 游戏的主启动类
 */
public class StartGame {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setBounds(200,25,900,720);
        jFrame.setResizable(false);//设置窗口大小不可变
        jFrame.setTitle("贪吃蛇小游戏");
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.add(new GamePanel());//将面板添加到窗口上
        jFrame.setVisible(true);
    }
}

class Data {
    //页眉
    public static URL headerURL = Data.class.getResource("static/header.png");
    public static ImageIcon header = new ImageIcon(headerURL);
    //蛇的方向
    public static URL upURL = Data.class.getResource("static/up.png");
    public static URL downURL = Data.class.getResource("static/down.png");
    public static URL leftURL = Data.class.getResource("static/left.png");
    public static URL rightURL = Data.class.getResource("static/right.png");
    public static ImageIcon up = new ImageIcon(upURL);
    public static ImageIcon down = new ImageIcon(downURL);
    public static ImageIcon left = new ImageIcon(leftURL);
    public static ImageIcon right = new ImageIcon(rightURL);
    //蛇的身体
    public static URL bodyURL = Data.class.getResource("static/body.png");
    public static ImageIcon body = new ImageIcon(bodyURL);
    //食物
    public static URL foodURL = Data.class.getResource("static/food.png");
    public static ImageIcon food = new ImageIcon(foodURL);
}

/**
 * 游戏的面板
 */
class GamePanel extends JPanel implements KeyListener, ActionListener {
    //定义蛇的属性
    int length;//蛇的长度
    int[] snake_x = new int[600];//蛇的x坐标
    int[] snake_y = new int[500];//蛇的y坐标
    String director;//记录蛇头的方向

    //定义食物的属性
    int food_x,food_y;//食物的坐标
    Random random = new Random();//食物随机产生坐标

    //积分属性
    int score;

    //游戏状态属性
    //记录当前游戏的状态
    boolean isStart = false;//默认停止
    //判断游戏失败
    boolean isFail = false;

    //事件监听，通过固定的事件进行刷新
    //定时器
    /*public Timer(int delay, ActionListener listener)*/
    Timer timer = new Timer(100,this);//delay越小，速度越快

    //构造器
    public GamePanel() {
        init();
        //获得焦点和键盘事件
        this.setFocusable(true);
        this.addKeyListener(this);
        timer.start();
    }

    //初始化方法，游戏的初始化状态
    public void init() {
        //蛇的初始化
        length = 3;
        snake_x[0] = 100;
        snake_y[0] = 100;//蛇头的坐标
        snake_x[1] = 75;
        snake_y[1] = 100;//蛇第一个身体的坐标
        snake_x[2] = 50;
        snake_y[2] = 100;//蛇第二个身体的坐标
        director = "R";//默认向右

        //食物的初始化
        //初始化食物的数据,将食物随机分布在界面上
        food_x=25+25*random.nextInt(34);
        food_y=75+25*random.nextInt(24);

        //积分的初始化
        score=0;
    }


    //绘制面板，游戏的所有东西都使用这个画笔来画
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);//清屏
        this.setBackground(Color.WHITE);
        //绘制静态的面板
        //绘制页眉
        Data.header.paintIcon(this, g, 25, 11);//放置页眉
        g.fillRect(25, 75, 850, 600);//默认游戏界面,默认黑色

        //绘制食物
        Data.food.paintIcon(this,g,food_x,food_y);

        //绘制小蛇
        //将小蛇画上去，蛇身体使用for循环添加，方向使用if判断
        if (director.equals("R")) {
            Data.right.paintIcon(this, g, snake_x[0], snake_y[0]);//蛇头初始化向右
        }
        if (director.equals("L")) {
            Data.left.paintIcon(this, g, snake_x[0], snake_y[0]);
        }
        if (director.equals("U")) {
            Data.up.paintIcon(this, g, snake_x[0], snake_y[0]);

        }
        if (director.equals("D")) {
            Data.down.paintIcon(this, g, snake_x[0], snake_y[0]);

        }
        for (int i = 1; i < length; i++) {
            Data.body.paintIcon(this, g, snake_x[i], snake_y[i]);
        }

        //判断游戏状态
        if (isStart == false) {
            g.setColor(Color.WHITE);//设置字体颜色
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));
            g.drawString("按下空格开始游戏", 300, 300);
        }
        if(isFail==true){
            g.setColor(Color.RED);//设置字体颜色
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));
            g.drawString("失败，按下空格重新开始", 300, 300);
        }

        //绘制积分显示
        g.setColor(Color.WHITE);
        g.setFont(new Font("微软雅黑", Font.BOLD, 18));
        g.drawString("长度"+length,750,35);
        g.drawString("分数"+score,750,50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(isStart&&isFail==false){
            //游戏开始，小蛇开始动

            //检测小蛇吃食物
            if(snake_x[0]==food_x&&snake_y[0]==food_y){
                length++;
                score+=10;
                //重新生成食物
                food_x=25+25*random.nextInt(34);
                food_y=75+25*random.nextInt(24);
            }

            //控制身体移动
            for(int i =length-1;i>0;i--){
                snake_x[i]=snake_x[i-1];
                snake_y[i]=snake_y[i-1];
            }

            //控制蛇头移动
            if(director.equals("U")){
                snake_y[0]-=25;
                //边界判断
                if(snake_y[0]<75){
                    snake_y[0]=650;
                }
            }
            if(director.equals("D")){
                snake_y[0]+=25;
                //边界判断
                if(snake_y[0]>650){
                    snake_y[0]=75;
                }
            }
            if(director.equals("L")){
                snake_x[0]-=25;
                //边界判断
                if(snake_x[0]<25){
                    snake_x[0]=850;
                }
            }
            if(director.equals("R")){
                snake_x[0]+=25;
                //边界判断
                if(snake_x[0]>850){
                    snake_x[0]=25;
                }
            }

            //失败判定，撞到自己
            for(int i=1;i<length;i++){
                if(snake_x[0]==snake_x[i]&&snake_y[0]==snake_y[i]){
                    isFail=true;
                }
            }
            repaint();
        }
        timer.start();
    }

    //实现键盘监听
    @Override
    public void keyPressed(KeyEvent e) {
        //监听键盘的按压事件
        //获得当前键盘按下的是哪一个
        int keyCode = e.getKeyCode();
        //空格键，游戏开始或者停止
        if (keyCode == KeyEvent.VK_SPACE) {
            if(isFail){
                //重新开始
                isFail=false;
                init();
            }
            if(!isFail){
                isStart = !isStart;
            }
            repaint();
        }

        //实现小蛇上下左右移动
        if(keyCode==KeyEvent.VK_UP){
            director="U";
        }
        if(keyCode==KeyEvent.VK_DOWN){
            director="D";
        }
        if(keyCode==KeyEvent.VK_LEFT){
            director="L";
        }
        if(keyCode==KeyEvent.VK_RIGHT){
            director="R";
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
}