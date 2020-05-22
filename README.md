# MyKline

一个可扩展 K 线控件（框架）

# 问题

之前多个项目中都用到了 K 线控件，结果设计师设计很刁钻，每个项目的 K 线的设计配色，细节，布局都有点不一样。

之前写的控件在于完成功能，并没有很好的一个架构，因此代码编写过程中几乎都是顺序逻辑，在遇到设计多变的情况，解决方案就是重写控件，复制相同逻辑的代码，重写很多细节代码，比如颜色，坐标计算，绘制细节

这对于自己是非常痛苦的，对其他人使用你的控件去修改成他们自己的控件也非常困难

# 使用

## 演示
![MyKline.gif](https://github.com/JohnZh/MyKline/raw/master/images/MyKline.gif)

## 例子

项目里面分别提供了如下例子：

- 全部使用默认的配置进行的控件展示
- 两个以上的指标绘制区
- 修改 k 线指标样式，例如蜡烛图粗细，实心空心的修改，线颜色的修改，辅助线样式的修改等
- 修改指标文字的样式位置
- 追加历史数据，最后一个数据的跳动
- 时间绘制区时间格式的修改
- 指标内小数位数的修改

## 先导知识

### 坐标系
控件坐标系为系统原始坐标系，左上角为 (0, 0) 点

```
(0,0) -------------> x axis / 越向右 x 值越大
|
|
|
|
|
∨
y axis / 越向下 y 值越大
```

### 设计
![MyKline.png](https://github.com/JohnZh/MyKline/raw/master/images/MyKline.png)

### 关键类与定义

- `KlineView`：主控件
- `DrawArea`：绘制区
	- `IndicatorDrawArea`：指标绘制区
	- `IndicatorTextDrawArea`：指标文字绘制区
	- `DateDrawArea`：时间轴绘制区
- `Indicator`：指标
	- `AbsIndicator`：抽象指标，内置指标从这继承，持有一个辅助线类
- `AuxiliaryLines`：辅助线
	- `SimpleAuxiliaryLines`：简单的实现 `AuxiliaryLines`
- `DrawDate`：绘制时间轴
	- `SimpleDrawDate`：最简单的实现 `DrawDate`
- `DetailView`：详情页面
	- `SimpleDetailView`：简单详情功能页面的实现，可直接使用，也可以作参考
- `Factory`：控件初始化工厂
	- `KlineFactory`：默认实现的工厂，作参考，也可继承使用
- `DATA`：K 线数据结构包装类，继承 KlineData，同时也方便获取任何类型的指标数据
- `inData`：指标数据类型的基类，任何指标需要的数据结构都需要继承该类

### 指标绘制流程
内置指标都继承自 `AbsIndicator`，`AbsIndicator` 继承 `Indicator`，`Indicator` 继承 `ValueRange`。

`Indicator` 在 `KlineView` 中执行流程依次为：

 - 计算指标
 - 计算辅助线
 - 绘制辅助线
 - 绘制指标

```
void calcIndicator(List<DATA> dataList, int startIndex, int endIndex);

void calcAuxiliaryLines(List<DATA> dataList, int startIndex, int endIndex);

void drawAuxiliaryLines(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint);

void drawIndicator(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint);

```

## 创建自己的 K 线控件

### 布局
```
<com.johnzh.klinelib.KlineView
        android:id="@+id/klineView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        app:layout_constraintTop_toTopOf="parent" />
```
如果需要通过双击或长按触发数据详情

```
<com.johnzh.klinelib.detail.SimpleDetailView
        android:id="@+id/detailView"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="@id/klineView"
        app:layout_constraintLeft_toLeftOf="@id/klineView"
        app:layout_constraintRight_toRightOf="@id/klineView"
        app:layout_constraintTop_toTopOf="@id/klineView" />
```
SimpleDetailView 并没有重写 onMeasure() 对尺寸进行固定，因此覆盖在 KlineView 即可

也可以自己继承 SimpleDetailView 或者 DetailView，创建个性化的详情页面

### 配置

```
binding.klineView.setConfig(
                new KlineConfig.Builder()
                        .initialCandles(40)
                        .activeDetailAction(DetailView.TRIGGERED_BY_DOUBLE_TAP)
                        .scale(new Scale())
                        .build());
```
依次为：

- 最初蜡烛柱数量
- 详情触发行为：双击或者长按
- 缩放范围

当前支持的配置很简单，因为大部分的配置会在设置绘图区（DrawArea）的时候

### 设置绘图区

绘图区的设置采用抽象工厂模式

```
Factory factory = new DefaultFactory(this);
binding.klineView.setDrawAreaList(factory);
```

```
public interface Factory {
    List<DrawArea> createDrawAreas();

    <T extends Indicator> T createDefaultIndex(Class<T> clazz);

    <T extends AuxiliaryLines> T createDefaultAuxiliaryLines(Class<T> clazz, int lines);
}
```

#### 实现自己的工厂

```
@Override
public List<DrawArea> createDrawAreas() {
    int textHeight = (int) dp2Px(TEXT_HEIGHT);
    int dataHeight = (int) dp2Px(DATA_HEIGHT);
    int dateHeight = (int) dp2Px(DATE_HEIGHT);
    int indexHeight = (int) dp2Px(INDICATOR_HEIGHT);

    IndicatorDrawArea indicatorArea1 = new IndicatorDrawArea(dataHeight, getIndicators1());
    IndicatorDrawArea indicatorArea2 = new IndicatorDrawArea(indexHeight, getIndicators2());

    List<DrawArea> drawAreaList = new ArrayList<>();
    drawAreaList.add(new IndicatorTextDrawArea(textHeight, indicatorArea1));
    drawAreaList.add(indicatorArea1);
    drawAreaList.add(new DateDrawArea(dateHeight, getDefaultDrawDate()));
    drawAreaList.add(indicatorArea2);
    drawAreaList.add(new IndicatorTextDrawArea(textHeight, indicatorArea2));
    return drawAreaList;
}
```
- 代码解释：
    - 确定每个区域的高度
	- 添加指标文字绘制区
	- 添加指标绘制区
	- 添加时间绘制区
	- 添加指标绘制区
	- 添加指标文字绘制区

详细可参考 `DefaultFactory` 代码

#### 添加指标绘制区和指标文字绘制区

```
IndicatorDrawArea indicatorArea1 = 
		new IndicatorDrawArea(dataHeight, getIndicators1());

protected List<Indicator> getIndicators1() {
    List<Indicator> list = new ArrayList<>();
    list.add(createDefaultIndex(PureKIndicator.class));
    list.add(createDefaultIndex(MAIndicator.class));
    list.add(createDefaultIndex(BOLLIndicator.class));
    return list;
}
```

```
// “指标文字绘制区” 需要关联一个“指标绘制区”
new IndicatorTextDrawArea(textHeight, indicatorArea1); 
```

### 实现自己的 k 线数据并添加进控件

```
public class MyKlineData implements KlineData {
	......
}

List<MyKlineData> klineDataList = getListFromServer();

List<DATA> dataList = DATA.makeList(klineDataList); 
// 或者逐个添加 dataList.add(new DATA(klineData))

binding.klineView.setDataList(dataList);

```

## 更多的实现参考给出的例子

# 内置指标算法

## MA（Moving Average）移动平均
当前数据的 MA(N)：以当前数据为结束点（包含在内），连续的 N 个数据收盘价 closePrice 的平均值，即：

```
// i 为当前下标
data[i].MA(N) = (data[i].closePrice + data[i - 1].closePrice + ... + data[i - N + 1].closePrice) / N
```

举例：下为第四个数据的 MA2；第四个数据的 MA5 由于数据不足，无法计算

```
// 下标从 0 开始
data[3].MA2 = (data[3].closePrice + data[2].closePrice) / 2
```
N 的取值一般使用 5，10，20，30，60

## BOLL（Bollinger Bands）布林线
分为 3 条线：阻力线 UPPER，中线 MID，支撑线 LOWER

- 中线 MID：一般为当前数据的 MA20 或者 MA26，暂记为 MA(N)
- 阻力线 UPPER = MID + 偏移值 OFFSET
- 支撑线 LOWER = MID - 偏移值 OFFSET
- 偏移值 OFFSET = P * MD
	-  P 为 BOLL 带宽度，一般取值为 2
	-  MD = STD(CLOSE - MA(N), N)，即以当前数据为结束点，连续 N 个数据收盘价的标准差

MD 公式：

```
X[i] = data[i].closePrice - data[i].MA(N)
MD = sqrt((X[i]^2 + X[i - 1]^2 + ... + X[i - N + 1]^2) / N)
```

## WR（Williams %R）

```
data[i].WR(N) = (HIGH(N) - data[i].closePrice) / (HIGH(N) - LOW(N)) * 100
```
- (HIGH(N) N 个数据里的最高价 `max(data[i].highestPrice)`
- LOW(N) N 个数据里的最低价 `min(data[i].closePrice)`
- 范围是以当前数据为结束点，包括当前数据在内的连续 N 个数据

##  EMA（Exponential Moving Average）指数移动平均值
MA 反应的是均价数值变化，EMA 反应的是均价趋势变化

```
标准定义：

假设 a = 2 / (N + 1)，即 1 - a = (N - 1) / (N + 1)

data[i].EMA(N) = a * ∑[k=0, ∞) (1 - a)^k * data[i-k].closePrice
```

k 越大，指数越小，k 越大数据越久远，k 越小代表数据越新，越近的数据权重越大，对计算结果数值影响越大

```
递推定义：

假设 a = 2 / (N + 1)，即 1 - a = (N - 1) / (N + 1)

data[i].EMA(N) = a * data[i].closePrice + (1 - a) * data[i - 1].EMA(N)
```
举例：第 7 个数据的 EMA(4)

```
// i 从 0 开始，data[0 - 6]
data[6].EMA(4) = 2/5 * data[6].closePrice + 3/5 * data[5].EMA(4)
data[5].EMA(4) = 2/5 * data[5].closePrice + 3/5 * data[4].EMA(4)
data[4].EMA(4) = 2/5 * data[4].closePrice + 3/5 * data[3].EMA(4)
data[3].EMA(4) = ?
```
在 MA4 里，第 4 个数据的 MA4 才是能按照公式计算得到的合法值，EMA4 应该也是如此

但是很多软件在对第一个数据取值上并没有统一

比如同花顺取了 data[3].closePrice 为 data[3].EMA(4)，作为整个递归的终点

有的软件则取了 data[3].MA(4) 作为 data[3].EMA(4)，作为整个递归的终点

而 data[3] 之前数据（data[0,1,2]）的 EMA(4) 没有意义

#### 那么有些软件上，数据不够的情况，EMA 是怎么算的呢？？
具体情况具体分析：比如 新浪财经的 M2009 的日 k [https://finance.sina.com.cn/futures/quotes/M2009.shtml](https://finance.sina.com.cn/futures/quotes/M2009.shtml)

- 第 1 条数据 EMA12：2795 等于该数据的收盘价
- 第 2 条数据 EMA12：2792.54 = 2779 * 2/13 + 2795 * 11/13，其中 2779 为该条数据收盘价
- 第 3 条数据 EMA12：2793.69 = 2800 * 2/13 + 2792.54 * 11/13, 其中 2800 为该条数据收盘价
- ......

但是这样的算法是否正确呢，并不能确定，因为常规算法是递归推算，而这样算完全可以正向记算了，不需要推算。但是 EMA 的正向计算，即标准公式是一个无限项的算法

此外它的 MA 的算法在数据不足的情况下的计算是明确不对的，比如计算 MA5，对于第 4 个数据，它直接使用计算了 MA4 作为 MA5


# License

	
	Copyright 2020 John Zhang
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	   http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.