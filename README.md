# PhotoMaster
图像RGBA 处理和 Matrix矩阵变化，像素点阵的分析，paint的画笔风格（Xformaodule和Shader的运算）

#1.RGBA(色光三原色) 的图像模型
##1.1基本原理
  R-->Red(红色色系参数)  G-->Green（绿色色系参数） B-->Blue(蓝色色系参数) A--alpha</br>
  附上一个色码表
  ![](https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1475228265&di=796ab70b10311242c72f963e37267621&src=http://i4.17173.itc.cn/fc/2008/07/09/20080419_735a4526ce44c5838eaa8rnWr9tRSKAZ.jpg)

  同时也要涉及到三个概念：色调，饱和度，亮度。
      `色调:用于传递颜色，也是就是实现RGB三原色效果`
      `饱和度：颜色的纯度从0%到100%进行变换，越大色彩效果越丰富`
      `亮度：颜色相对的明暗程度，最低为全黑色，最高全为白色`
##1.2 在android中实现RGBA的色光三原色效果
   在android的api中提供了ColorMatrix类来实现对RGBA模型改变照片效果的方法。</br>
           setRotate(Int,float) :int 为0代表red，1代表green，2代表blue
           setSaturation(float):设置饱和度
           setScale(float,float,float,int)：设置
   
  
  
