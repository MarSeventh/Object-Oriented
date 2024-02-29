#本仓库包含OO四个单元作业及上机内容，分别位于不同分支中，请切换分支查看！
# OO第一单元总结

---

## 目录

[TOC]



## 前言

在经历了第一单元三次作业的~~(蹂躏)~~磨练之后，笔者终于从一个OO和Java的**小白**晋升为**萌新**了。回想起自己第一周开始做作业时由于缺乏Java的基础知识而一连几天毫无进展，最后连续鏖战将近二十个小时才勉强完成任务时的~~狼狈~~的样子，不免有一种 _“忆往昔峥嵘岁月稠”_的感觉，令人心中百感交集。

~~好了，不说废话了()~~

接下来，笔者将对自己第一单元三次作业进行一个较为全面的复盘，分别对三次作业的架构、代码的复杂度、类间关系、强测和互测反映出来的bug等进行总结和分析。

##  一、Homework1

* ### 架构思路

  对于第一次作业的架构笔者思考了整整两天，不可谓不折磨~~(其实主要是因为不懂Java，hhhh)~~，最终笔者决定第一次课上实验时课程组所给的advance例程中的架构，也就是主流的递归下降方式。该架构的主要思想是对输入的表达式抽象化、层次化，将其抽象为表达式、项、因子三个层次，表达式由项组成，项则由因子组成，在处理时通过递归的方式逐层深入处理，处理到因子层次时，根据所遇因子的不同种类选择不同的解析方法，解析后返回因子，本次递归结束。

  这一架构相对于字符串解析的方式具有很大优势。第一大优势就在于其容错性和可读性较高，我们可以很清楚地看到每一层的行为，遇到程序出错时也比较好定位到出错的具体层级和方法。与此同时，这种架构不容易出现细节性的bug~~(理论上是这样的)~~，可以有效避免很多不容易测出来的小问题(比如采用字符串解析时需要考虑到符号的顺序等各种细节，而递归下降实际将很多东西交由程序自动处理)。第二大优势则在于代码可扩展性很强，这一点在后两次作业中展现得尤其明显。新增加的很多要求，大多都可以通过增加Factor因子种类的方式来解决。并且类似于嵌套括号的问题，甚至不需要进行额外的考虑，架构本身就已经将嵌套括号的情况囊括其中。
  
* ### 类间关系

  ![hw1](https://alist.sanyue.site/d/imgbed/202311252242136.png)

  如图所示，本次作业主要移位器**Lexer**和解析器**Parser**组成解释模块，并且将表达式抽象为**Expr**、**Term**和**Factor**三个层次，其中**Factor**为接口，在本次作业中**Expr**、**Term**、**Variable**和**Number**四个类实现了该接口。

* ### 代码复杂度分析

  **Statistic**

  ### ![image-20230317195055986](https://alist.sanyue.site/d/imgbed/202311252242678.png)

  **Method metrics**

  ![image-20230317195630114](https://alist.sanyue.site/d/imgbed/202311252242778.png)

  ![image-20230317195737321](https://alist.sanyue.site/d/imgbed/202311252242039.png)

  **Class Metrics**![image-20230317195823422](https://alist.sanyue.site/d/imgbed/202311252242963.png)

  在方法复杂度分析中可以看到，Expr的merge、simplify，Parser的parseTerm和Term的orgnizeFactor这几个方法的复杂度都比较高，因为这几个方法中实现了太多的功能，并且没能很好地将其中的某些部分提炼为单独的方法，导致这几个方法较为臃肿。

  在类复杂度分析中可以看出Expr、MainClass、Parser和Term的复杂度都比较高，这也是因为这几个类中实现的方法较多，例如在MainClass中不仅实现了函数入口main，还实现了对表达式进行预处理等功能。

* ### bug分析

  第一次作业写完之后没有出现什么严重的bug，强测也得到了较为不错的成绩，而互测中唯一被hack成功的bug是因为超时导致的，这一bug的原因是我在进行最后的长度优化时，为了将式子第一项变成正数采用了太多次循环导致的，这一点也是我设计过程中出现的疏漏，修改了这一部分循环之后也成功通过了互测样例。

## 二、Homework2

* ### 架构思路

  第二次作业在第一次作业的基础上增加了嵌套括号的支持以及自定义函数、三角函数两类因子，也是一次相当复杂的迭代。前文笔者提到过，递归下降的架构具有很好的可扩展性，这一点在这次迭代中便展现得淋漓尽致。首先是嵌套括号的支持，第一次作业的架构已经很好地帮我们实现了~~(乐)~~。其次是自定义函数和三角函数这两类因子，我们本质上只是再多设计两个实现**Factor**接口的类罢了。很美好对吧？

  hhh，但是说来简单做起来难。在实现自定义函数的过程中，笔者新定义了**SelfDefine**类用于实现自定义函数定义式的解析以及处理，在解析表达式时若遇到自定义函数，便直接开始对自定义函数处理后的表达式进行解析，这本质是另外一套解析表达式的方案，即遇到相关的变量便将传入的参数返回作为因子，处理完后再将整个表达式作为表达式因子返回。在三角函数处理的过程中，我们只需要将三角函数括号内部分作为表达式进行解析，之后加上三角函数名返回即可，相对于自定义函数较为简单一些。

  _但是！但是！！但是！！！_

  ~~三角函数的化简却是我一生之痛。~~三角函数的化简还是颇有难度的，笔者在这部分仅仅处理了sin^2+cos^2=1这部分就花了整整两天时间，并且还只能做到半成品。对这部分的化简，我的大致思路是将平方项取出存入ArrayList中，然后将这一项的Hashmap整个存入另一个ArrayList的相同位置中，之后每次出现平方项就遍历存储平方项的ArrayList，如果有匹配的平方项再比较其系数是否一致，若一致则可进行消除操作，改变对应项的Hashmap以及相应系数。

* ### 类间关系

  ![hw2](https://alist.sanyue.site/d/imgbed/202311252242054.png)

  从类图中我们可以看到，这次作业的架构比上次复杂了不少，主要是由于新增了两个因子导致的。Expr内部方法的显著增多一方面是由于化简三角函数时使用了大量方法，另一方面时笔者对第一次作业的架构进行了优化，将一些原有的代码抽象成了新的方法，提高了代码的可读性和可维护性。

* ### 代码复杂度分析

  **Statistic**

  ![image-20230317205825236](https://alist.sanyue.site/d/imgbed/202311252242003.png)

  我们可以看到，代码的总行数达到了809行，基本是第一次作业的两倍，可见这次迭代的量还是很大的。

  **Method metrics**

  ![image-20230317210033870](https://alist.sanyue.site/d/imgbed/202311252242685.png)

  ![image-20230317210059711](https://alist.sanyue.site/d/imgbed/202311252242474.png)

  ![image-20230317210123536](https://alist.sanyue.site/d/imgbed/202311252242437.png)

  从图中可以看出，除了第一次作业出现的复杂度较高的方法外，Expr的TriMatchPro方法也非常高，而该方法复杂度较高的原因是它作为三角函数化简最重要的方法，多次调用了其他方法，并且自身存在递归调用。

  **Class Metric**

  ![image-20230317211628874](https://alist.sanyue.site/d/imgbed/202311252242120.png)

  从中我们看到，**SelfDefine**作为新增的自定义函数类，其复杂度也是达到了较高的水准，这是因为其中不仅实现了函数表达式的解析，还实现了参数处理，函数调用处理等一系列方法。

* ### bug分析

  本次作业的bug还是比较惨痛的，笔者由于花了太多的时间对三角函数进行化简，导致没能对基础的功能进行充分的测试，使得一个很大的漏洞没有被发现，强测最终只拿到了很低的分数，互测也被狠狠地hack了（悲）。

  这次作业的bug其实也不是很隐蔽，因为我在返回三角函数因子时将其内部的表达式化为了最简形式，也就代表着^被重新变成了**，而在Term中进行合并时又进行了一次因子的解析，这就导致再次解析到幂次时他不认识了，抛出了异常。

  总体来说，这个bug并不难发现，完全是没有充分测试造成的。其次，这个bug也与架构有一定的关系，说明我的架构还不是很简洁，出现了冗余的调用和重复的操作。

## 三、Homework3

* ### 架构分析

  本次作业新增内容不多，仅仅增加了一个求导因子以及自定义函数的嵌套调用。这里笔者就不得不再提一下递归下降架构的优越性，因为对于递归下降架构而言，自定义函数的嵌套调用已经实现了~~（对！就是已经实现了hhh）~~。所以我们只用集中火力攻克求导因子的问题就可以了。

  其实笔者这次作业做的并不算顺利，因为开始并没有将递归下降的精髓移植到求导上来，一心想要通过字符串解析的方式进行求导运算，导致笔者很长时间都非常痛苦，写了好几百行自己都不愿意去看的求导代码，并且后面还有很大一部分尚未完成。于是笔者最终还是选择放弃了字符串解析的方法，将递归下降的方法照猫画虎地应用在了求导上面，只是最后解析因子时返回值变为导数，然后在Term中处理时对项应用乘法法则，将一个表达式整体返回。结果这种架构果然不负所望，很容易就完成了之前几百行代码也没能完成的工作，并且结构非常的清晰。

* ### 类间关系

  ![hw3](https://alist.sanyue.site/d/imgbed/202311252242510.png)

  从图中可以看出，由于增加了求导因子，导致类图又~~“凌乱”~~了许多。但是仔细观察我们还是能够看出来，主要增加的内容就是**DeSin**、**DeCos**、**DeVariable**、**Derivative**等求导因子。

* ### 代码复杂度分析

  **Statistic**

  ![image-20230317215801020](https://alist.sanyue.site/d/imgbed/202311252242867.png)

  从图中可以看出，这次作业的代码行数来到了1376行，相比上次又增加了将近600行。emmm，习惯了。

  **Method Metric**

  ![image-20230317220130466](https://alist.sanyue.site/d/imgbed/202311252242167.png)

  ![image-20230317220207112](https://alist.sanyue.site/d/imgbed/202311252242515.png)

  ![image-20230317220226648](https://alist.sanyue.site/d/imgbed/202311252242306.png)

  ![image-20230317220243025](https://alist.sanyue.site/d/imgbed/202311252242570.png)

  这次的方法复杂度分析，最直观的就是方法的总数有了明显的提升，一个原因是本次求导新增了不少方法，另一个原因是笔者对之前的代码再次进行了优化，提炼了更多的方法，提高了代码的可读性。所以我们可以看到，几个之前复杂度较高的方法的复杂度都降了一半以上。并且可以看出，有多个方法耦合度较高，说明方法之间联系过于紧密。

  **Class Metric**![image-20230317220608916](https://alist.sanyue.site/d/imgbed/202311252243318.png)

  从该图可以看到，新增的类没有出现复杂度过高的情况。

* ### bug分析

  本次作业较为顺利，除了在自己测试时测出了dz不能求导这种睿智的bug之外，在强测中没有发现其他的bug，但是互测被hack了3刀，均是因为没有考虑到当自定义函数代价过高时会超时和超内存的问题。修改的方法也是将自定义函数的解析放到**调用时**进行。

## 四、心得体会

第一单元作业可谓是完成的历经坎坷，让笔者从假期的安逸状态最快速度地进入到了高度紧张的学习状态，并且在短短几天内熟悉了Java的基本语法和IDEA的基本操作，虽说是过程比较心酸，但是收获确实颇丰，而且这段经历必定会深深地烙印在笔者的心里。

从第一单元的作业中，我明白了一个良好的架构的重要性，因为开始选择了正确的架构，所以第二次作业的迭代没有出现太大范围的重构，但是第三次作业错误架构的选择也让我一定程度上尝到了重构的苦头。以后进行工程开发时，首先应该对架构有一个明确的认识和规划，然后再进行操作，这样可以最大程度地避免大范围重构的可能，并且提高代码的可维护性和可扩展性。

除此之外，我还悟出了一些bug测试的方法。首先就是逐渐缩小出现bug的样例，找到稳定复现bug的部分，分析出其中与其他样例不同的地方，然后回到代码中找这部分地方对应的代码段，并通过调试找出漏洞。其次就是应该多测试一些边缘化的数据，例如第一次和第三次互测中tle的数据均为cost边缘的数据，由此可见边缘化的测试非常重要。

希望在接下来的OO旅程中，我能汲取这个单元的作业和教训，学到更多的知识和技能，取得更好的成绩。