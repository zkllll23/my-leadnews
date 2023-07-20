<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>inner Function</title>
</head>
<body>

<b>算数运算符</b>
<br/><br/>
100+5 运算：  ${100 + 5 }<br/>
100 - 5 * 5运算：${100 - 5 * 5}<br/>
5 / 2运算：${5 / 2}<br/>
12 % 10运算：${12 % 10}<br/>
<hr>

<b>比较运算符</b>
<br/>
<hr>
<br/>

<dl>
    <dt> =/== 和 != 比较：</dt>
    <dd>
        <#if "xiaoming" == "xiaoming">
            字符串的比较 "xiaoming" == "xiaoming"
        </#if>
    </dd>
    <dd>
        <#if 10 != 100>
            数值的比较 10 != 100
        </#if>
    </dd>
</dl>



<dl>
    <dt>其他比较</dt>
    <dd>
        <#if 10 gt 5 >
            形式一：使用特殊字符比较数值 10 gt 5
        </#if>
    </dd>
    <dd>
        <#-- 日期的比较需要通过?date将属性转为data类型才能进行比较 -->
        <#if (date1?date >= date2?date)>
            形式二：使用括号形式比较时间 date1?date >= date2?date
        </#if>
    </dd>
</dl>

<br/>
<hr>

<b>逻辑运算符</b>
<br/>
<br/>
<#if (10 lt 12 )&&( 10  gt  5 )  >
    (10 lt 12 )&&( 10  gt  5 )  显示为 true
</#if>
<br/>
<br/>
<#if !false>
    false 取反为true
</#if>
<hr>

<b>获得集合大小</b><br>

集合大小：${stus?size}
<hr>


<b>获得日期</b><br>

显示年月日: ${today?date}       <br>

显示时分秒：${today?time}<br>

显示日期+时间：${today?datetime}<br>

自定义格式化：  ${today?string("yyyy年MM月")}<br>

<hr>

<b>内建函数C</b><br>
没有C函数显示的数值：${point} <br>

有C函数显示的数值：${point?c}

<hr>

<b>声明变量assign</b><br>
<#assign text="{'bank':'工商银行','account':'10101920201920212'}" />
<#assign data=text?eval />
开户行：${data.bank}  账号：${data.account}

<hr>
</body>
</html>