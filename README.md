实现思路很简单左侧栏目是一个一个的 Fragment 的，点击时动态替换各个 Fragment 到当前 Activity 中。
<p>Fragment fragment = (Fragment) cls.newInstance();<br/>
// Insert the fragment by replacing any existing fragment
FragmentManager fragmentManager = getSupportFragmentManager();<br/>
fragmentManager.beginTransaction().replace(R.id.fragment_content, fragment, tag).commit();</p>

依赖 SliddingMenu   ViewPagerIndicator

首页布局：

1 SliddingMenu + ViewPagerIndicator

2 JSON 解析 FastJson

3 网络请求 Volley

4 sqlite 数据库简单封装，主要处理数据库版本升级问题

5 微信、微博 API 简单封装

6 代码混淆
