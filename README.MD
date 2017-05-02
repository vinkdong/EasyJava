![EasyJava](http://vinkdong.com/images/logo.png)
####EasyJava框架--快速构建网页
一个快速用java搭建的网页框架

向所有给予灵感的框架设计师致敬！

##EasyJava将有有哪些功能？

* 快速的`网页`构建
    *  一句话生成网页快速明了
    *  美观大气上档次

## 使用maven引入EasyJava
```
<repositories>
        <repository>
            <id>vinkdong</id>
            <name>VinkDong Maven</name>
            <url>http://vinkdong.com/repo/maven2/</url>
            <layout>default</layout>
        </repository>
</repositories>
<dependencies>
        <dependency>
            <groupId>org.easyjava</groupId>
            <artifactId>easyjava_web</artifactId>
            <version>0.2</version>
        </dependency>
</dependencies>
```

##官方网站：[EasyJava.org](http://easyjava.org)



---
DICT:

```java
import org.easyjava.util.*;
import org.easyjava.file.Dict;
...
    @Test
    public void testDict(){
        Dict a = new Dict();
        a.update("{a:100,b:{c:90},c:yy,d:o9}");
        System.out.println(a.get("a"));
        System.out.println(a.get("b"));
        System.out.println(a.getDict("b").get("c"));
        EOut.print(a.getKeys());
        System.out.println(a.get("c"));
    }
```
result:   

```
100
{c:90}
90
1:a
2:b
3:c
4:d
yy

```
# 
##意见反馈
有更好的建议可以通过以下联系给我建议和建议

* 邮件:master@easyjava.org
* weibo: [@VinkDong](http://weibo.com/donvinkd)
* facebook: [@DonvinkDong](http://facebook.com/donvinkd)


##关于作者

```java
  vink = {
    Name  : "VinkDong",
    site : "http://vinkdong.com"
    mail:"dong@wenqi.us"
  }
```