###透明状态栏
这里采用第三方的库 SystemBarTintManager 实现状态栏的透明，以达到沉浸式的效果。

如下效果图：

![img2](https://github.com/imtianx/StudyDemoForAndroid/blob/master/A01-statusbardemo/art/1.jpg)
![img2](https://github.com/imtianx/StudyDemoForAndroid/blob/master/A01-statusbardemo/art/2.jpg)

<br/>
具体使用：<br/>

* 导入 [SystemBarTintManager](https://github.com/imtianx/StudyDemoForAndroid/blob/master/A01-statusbardemo/src/main/java/cn/imtianx/statusbardemo/SystemBarTintManager.java) 类 <br/>
* 在要设置为沉浸式的activity的onCreate()方法中添加如下代码：<br/>
```
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) 
	{
		setTranslucentStatus (true);
		SystemBarTintManager tintManager = new SystemBarTintManager (this);
		tintManager.setStatusBarTintEnabled (true);
		// 通知栏颜色
		tintManager.setStatusBarTintResource (R.color.titlec);
```		
<br/>并在onCreate()外添加如下方法：<br/>
```
    private void setTranslucentStatus(boolean on) {
   		Window win = getWindow();
   		WindowManager.LayoutParams winParams = win.getAttributes();
   		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
   		if (on) {
   			winParams.flags |= bits;
   		} else {
   			winParams.flags &= ~bits;
   		}
   		win.setAttributes(winParams);
   	}
```
* 最后在activity 的跟布局中添加以下两个属性：<br/>
```
	android:fitsSystemWindows="true"
	android:clipToPadding="true"
```
