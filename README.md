# FastImageSize
[ ![Download](https://api.bintray.com/packages/qstumn/maven/FastImageSize/images/download.svg) ](https://bintray.com/qstumn/maven/FastImageSize/_latestVersion)

FastImageSize是一个快速获得网络图片尺寸信息和图片类型的android libary

FastImageSize通过读取图片文件头解析图片信息不需要完全下载图片，内部建有三级缓存，可以快速轻松的实现设置占位图尺寸、图片流式布局、动态调整View尺寸等功能。
    
## how to use:
### 1.gradle
```groovy
     compile 'q.rorbin:FastImageSize:1.0.3'
```
   VERSION_CODE : [here](https://github.com/qstumn/FastImageSize/releases)
### 2.code
```java
int[] imageSize;
//sync
imageSize = FastImageSize.with(url).get();

//async
FastImageSize.with(url).get(new ImageSizeCallback() { 

       @Override   
       public void onSizeReady(int[] size) { 
            imageSize = size;
       }
});

int imageWidth = imageSize[0];
int imageHeight = imageSize[1];
int imageType = imageSize[2];
```

  或者你需要FastImageSize根据图片尺寸帮你设置View宽高 ：
```java
FastImageSize.with(url).into(imageView);
FastImageSize.with(url).override(500).into(imageView);
```
  override方法可以根据你提供的限制值对宽高结果进行等比例缩放


  FastImageSize默认使用UrlConnection获取InputStream读取图片的文件头，可以替换成你自己使用的第三方库，按以下方法调用即可
```java
FastImageSize.with(url).customProvider(yourProvider).get();
```

  FastImageSize默认使用LruCache和DiskLruCache实现内存缓存和磁盘缓存，如果你不需要缓存每次都要重新读取，按以下方法调用即可
```java
FastImageSize.with(url).setUseCache(false).into(imageView);
```
### 3.permission
```xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

### 4.support
  JPEG GIF BMP PNG 
  
## Thanks For

* https://github.com/sdsykes/fastimage

# LICENSE
```
MIT License
```
