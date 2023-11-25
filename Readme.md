# OO第三单元总结

[TOC]

~~_盼望着，盼望着，OO第三单元结束了_~~

这一单元主要训练我们基于规格进行程序化设计的能力，主要要求我们能够根据程序的需求设计相应的方法规格、类规格，根据规格书写代码，最后基于规格开展测试。

尽管本单元课程主要强调我们理解JML规格并根据其正确书写代码的能力，但是通过三次作业的强测我们可以看到，其实正确实现JML要求的功能仅仅是本单元作业的基础，在实现JML要求的基础上择优选择数据结构、算法以提高程序的性能也是本单元作业中非常重要的一环。下面，我就从测试、架构、作业出现问题、OKtest方法分析等角度来对本单元的作业加以总结。

## 一、测试过程分析

在本单元中，我们的作业要求基本都是通过JML规格提供的，因此正确理解JML规格并且根据其要求编写测试数据对于我们程序正确性的保证至关重要。接下来，我将会谈谈我对一些测试方法的理解以及分享我自己采用的测试方法。

### 1、黑箱测试、白箱测试

黑箱测试和白箱测试是软件测试的两种常用方法。

用一种比较官方的说法表达的话，黑箱测试就是在不考虑程序内部结构和实现细节的情况下，通过输入数据和对应的预期输出来测试软件系统的功能、性能和安全等方面的质量特性。测试人员只关注软件接口的输入输出行为，而不需要了解程序的具体实现过程。而白箱测试则是指在了解程序内部结构和实现细节的基础上，通过各种测试手段来验证软件系统的正确性、稳定性、安全性、性能等方面的质量特性。测试人员需要了解代码实现、算法、数据结构、框架等技术细节，并通过编写测试脚本、调试器、静态和动态分析工具等进行测试。

通俗来讲，黑箱测试就是说我们只需要作为一个提供需求的用户对程序员写好的代码进行测试，针对我们希望这个程序达到的功能提供各种各样的测试数据即可。白箱测试就要求我们从程序员的视角出发，在已经知道代码实现细节的前提下，针对代码中所有可能输入的数据进行覆盖测试，同时对可能出现问题的地方进行重点测试，在这个过程中可以综合利用测试程序、脚本等来提高测试强度和覆盖率。

### 2、单元测试、功能测试、集成测试、压力测试、回归测试

**单元测试**：单元测试是针对代码中最小的可测试单元（例如函数、方法）进行的测试，目的是验证这些单元的行为是否符合预期。例如我们在做第二次作业时对modifyRelation方法进行验证，可以先考虑modifyRelation执行后可能出现的情况（边的value增加或减小，边被删除，抛出异常），然后针对这几种情况来编写测试数据来测试该方法的正确性。

```java
public void modifyRelation(int id1, int id2, int value) throws PersonIdNotFoundException,
            EqualPersonIdException, RelationNotFoundException {
        if (contains(id1) && contains(id2) && id1 != id2
                && getPerson(id1).isLinked(getPerson(id2))) {
            MyPerson person1 = (MyPerson) getPerson(id1);
            MyPerson person2 = (MyPerson) getPerson(id2);
            person1.modifyAcq(person2, value);
            person2.modifyAcq(person1, value);
        } else {
            if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else if (!contains(id2)) {
                throw new MyPersonIdNotFoundException(id2);
            } else if (id1 == id2) {
                throw new MyEqualPersonIdException(id1);
            } else {
                throw new MyRelationNotFoundException(id1, id2);
            }
        }
    }
```

**功能测试**：功能测试是通过模拟用户使用软件来验证整个应用程序是否按照用户需求正确工作的测试。功能测试通常涉及多个组件和系统，它可以手动或自动执行。也就是说，功能测试类似于上文中提到的黑箱测试，我们需要从用户的视角来看，给出任意合法的输入和预期输出，与程序输出进行比较，从而判断应用程序的正确性。

**集成测试**：集成测试是将多个单元或组件组合起来以检查它们之间的交互是否如预期般工作的测试。它可以确保各组件之间的接口一致性和兼容性。集成测试主要目的是测试多个模块之间的数据通信，因为尽管每个模块都通过了单元测试，但是由于不同模块之间的接口，异常处理方式依然可能存在问题，所以需要将多个模块组合起来测试它们协同工作的能力。

**压力测试**：压力测试是通过模拟真实环境下大量用户访问应用程序来测试应用程序性能和稳定性的测试。该测试可以帮助识别应用程序在高负载情况下的响应时间和资源使用情况，并确定其能够承受多大的负载。简单来说，我们作业的强测环节就是一种压力测试，在输入大量数据的情况下来判断程序的正确性，同时用CPU时间来衡量程序的性能，有效测试了我们的程序在高负载情况下的运行能力。

**回归测试**：回归测试是在对软件进行更改后重新运行先前已经运行过的测试，以确保没有已有功能被意外地破坏。它可以帮助开发人员快速发现并修复由新更改引入的错误，以确保软件的质量和稳定性。也就是说，我们在修改bug或者是修改算法来提高程序性能时，有没有写出新的bug(bushi)，通常这种情况是很常见的，所以我们必须在修改一次程序之后就要用之前测试过的数据来对修改后的程序加以测试，来验证新程序是否具有一定的正确性。使用Gitlab中的CI/CD管道就可以很好地对我们的代码开展回归测试。

### 3、测试工具的使用

本单元作业中，课程组建议我们使用Junit方法开展测试，但是由于笔者的IDEA无法安装对应插件，所以只好对这种方法浅尝辄止，取而代之的是利用dalao的数据生成器和测试脚本开展的随机测试。测试思路就是利用数据生成器随机生成大量测试数据，然后使用对拍脚本来对多个jar包的输出结果进行对拍，发现不同输出之后就可以进一步寻找bug的位置。

数据生成器和测试脚本如下（均为大佬所写，这里仅列举一些大致思路，详细内容请移步[OO_hw10_数据生成器&简易对拍脚本 | ZJYの颓圮日记 (jingyuchou.github.io)](https://jingyuchou.github.io/2023/05/06/oo-hw10-shu-ju-sheng-cheng-qi-jian-yi-dui-pai-jiao-ben/)）

```C
//数据生成器
int main() {
    int num = 100;
    while (num-- > 1) {
        printf("%d\n", num);
        num_person = num_message = num_group = 0;
        char filename[100];
        memset(filename, 0, 100);
        memset(group_len, 0, 500 * sizeof(int));
        memset(groupPerson, 0, 500 * 1111 * sizeof(int));
        memset(hash, 0, 5000 * 5000 * sizeof(int));
        char nums[100];
        strcat(filename, "hw10datas\\fileIn");
        itoa(num, nums, 10);
        strcat(filename, nums);
        strcat(filename, ".txt");
        FILE *out = fopen(filename, "w");
        for (int i = 0; i < 10; i++) {
            AddPerson(out);
        }
        for (int i = 0; i < 100; i++) {
            generator(out);
        }
    }
    return 0;
}
```

```shell
//测试脚本
$y = 0
for ($x = 0; $x -lt 100; $x=$x+1)
{
    cat ".\hw10datas\fileIn$x.txt" | java -jar hw_10.jar > ".\hw10_out\stdout$x.txt"
    cat ".\hw10datas\fileIn$x.txt" | java -jar xxx.jar > ".\hw10_out_xxx\stdout$x.txt"
    if($(diff  (cat ".\hw10_out\stdout$x.txt") (cat ".\hw10_out_xxx\stdout$x.txt"))) {
        echo "------------------------------------------------------------"
        echo "$x xxx and zjy are different" 
        cat ".\hw10_out\stdout$x.txt" > ".\error1\stderr_xxx_$x.txt"
        cat ".\hw10_out_xxx\stdout$x.txt" > ".\error1\stderr_xxx_$x.txt"
        cat  ".\hw10datas\fileIn$x.txt" >  ".\error1\stderrInput$x.txt"
        echo "------------------------------------------------------------"
        $y=$y+1
    }else {
        echo "$x xxx and xxx are same"
    }


if(!$y){
echo "NO_ERROR_THIS_TIME"
echo "Congratulation ! xxx xxx xxx are same in the test of $x"
}
```

### 4、数据构造策略

本次作业的数据构造可以从两个方面出发，第一个方面是正确性角度，第二个方面是性能角度。对于正确性测试而言，由于对于一个社交关系网络，它的人数多少不会影响到我们所实现功能的正确性。也就是说只要我们的程序在少数人的情况下可以正确输出，那么对于多数人而言也大概率是正确的。因此对于不同的方法，我们只需要构造基于少数人的不同情况样例即可。举个例子，在对于第三次作业中的deleteColdEmoji方法，我们就可以采用以下的简单样例来测试其正确性：

```
ap 1 1 1
ap 2 2 2
ar 1 2 1
sei 1
aem 4 1 0 1 2
sm 4
sei 2
aem 5 1 0 1 2
aem 6 1 0 1 2
aem 7 2 0 1 2
dce 1
sm 5
aem 8 1 0 1 2
sm 7
```

该样例同时测试了dce方法是否能将heat低于limit的emoji同时从messages和emojiList中删除，但是又正确保留了heat>=limit的表情消息。

对于性能角度的测试，我们显然需要使用包含大量成员的社交网络，这就必须要用到数据生成器，并且在生成随机数据的时候，我们还需要权衡网络中人数的多少，边的密集程度，各个询问指令的数量和比例等，可以加大一些时间复杂度较高的指令的占比来测试程序的承压能力。除此之外，还需要设计好不同条件下图的整体结构，例如在第三次作业中测试queryLeastMoments时，我们除了考虑稠密图之外，还需要对稀疏图进行考虑，否则的话我们就难以发现dijkstra算法在稀疏图下表现不佳的问题，也难以想到用小顶堆的方式去优化dijkstra算法。

## 二、架构设计梳理

本单元作业中我的图模型均基于第一次作业的Block结构，也就是借鉴了一部分并查集的思想，将有关系的人都放在一个Block当中，在加边和删边时对Block进行维护，这样一来一个Block中的人都是连通的，就可以将qbs这类方法的复杂度降到O(1)级别。具体对Block进行维护的方式也比较简单，就是在添加边的时候判断两个人是否在一个Block中，如果不在就将两个Block进行合并；删边则稍微复杂一些，我们需要以被删掉边的一个端点出发去遍历所有这个点现在可达的节点，如果可达节点与原来Block中的节点完全相同，那么Block无需变化，否则说明被删掉的边是两个连通子图的“桥”，这样就要把原来的Block分裂为两个新的Block。实际操作如下：

```java
public void manageBlock(int id1, int id2) {
        HashMap<Integer, Person> block1 = new HashMap<>();
        for (HashMap<Integer, Person> block : blocks) {
            if (block.containsKey(id1)) {
                block1 = block;
                break;
            }
        }
        if (!block1.containsKey(id2)) {
            HashMap<Integer, Person> block2 = new HashMap<>();
            for (HashMap<Integer, Person> block : blocks) {
                if (block.containsKey(id2)) {
                    block2 = block;
                    blocks.remove(block);
                    break;
                }
            }
            block1.putAll(block2);
        }
    }
public void deleteBlock(int id1, int id2) {
        HashMap<Integer, Person> dblock = null;
        for (HashMap<Integer, Person> block : blocks) {
            if (block.containsKey(id1) && block.containsKey(id2)) {
                dblock = block;
                break;
            }
        }
        if (dblock != null) {
            HashMap<Integer, Person> newblock = new HashMap<>();
            getOneBlock(id1, dblock, newblock);
            if (!newblock.containsKey(id2)) {
                dblock.keySet().removeAll(newblock.keySet());
                blocks.add(newblock);
            }
        }
    }
```

除此之外，对于其他时间复杂度较高的方法，还有一个重要的思路，即**“动态维护”**，其基本思想就是我在改变社交网络的同时就对一些可能被查询到的数据进行维护，这样最后查询的时候只需要返回相应的数据即可，时间复杂度直接降到O(1)。尽管在维护的过程中可能会占用一些时间，但是在多数情况下我们查询的指令数都要比社交网络维护的指令数更多（这也符合现实情况），因此采用动态维护一定是一种占优策略。而动态维护的方法就要根据不同的指令的要求来具体实现了，例如对于qts，我们可以在每次加边时判断这两点是否和Block中的第三点都有关系，来增加qtsum，删边时则需要再次判断Block中和这两点都有关系的点，然后减小qtsum。对于qcs指令，因为每一次加边或者删边影响到的人数最多是四个人(即这条边的两个端点，以及可能存在的这两个人关系最好的两个人)，只要判断这四个人bestacquaintance的情况就能计算出qcsum的变化。具体实现如下：

```java
public void manageCouple(int id1, int id2, int oldcp1, int oldcp2) {
        MyPerson person1 = (MyPerson) getPerson(id1);
        MyPerson person2 = (MyPerson) getPerson(id2);
        int newcp1 = person1.getBestAcquaintance();
        int newcp2 = person2.getBestAcquaintance();
        MyPerson new1 = (MyPerson) getPerson(newcp1);
        MyPerson new2 = (MyPerson) getPerson(newcp2);
        MyPerson old1 = (MyPerson) getPerson(oldcp1);
        MyPerson old2 = (MyPerson) getPerson(oldcp2);
        if (oldcp1 != newcp1 || oldcp2 != newcp2) {
            if (newcp1 == id2 && newcp2 == id1) {
                cpsum++;
            }
            if (oldcp1 == id2 && oldcp2 == id1) {
                cpsum--;
            }
            if (newcp1 != oldcp1) {
                if (newcp1 == id2) {
                    if (old1 != null && old1.getBestAcquaintance() == id1) {
                        cpsum--;
                    }
                } else {
                    if (new1 != null && new1.getBestAcquaintance() == id1) {
                        cpsum++;
                    }
                }
            }
            if (newcp2 != oldcp2) {
                if (newcp2 == id1) {
                    if (old2 != null && old2.getBestAcquaintance() == id2) {
                        cpsum--;
                    }
                } else {
                    if (new2 != null && new2.getBestAcquaintance() == id2) {
                        cpsum++;
                    }
                }
            }
        }
    }
```

## 三、性能问题及修复情况

如果说根据JML规格写代码的话，相信我们都能很轻松地写出来，完成程序指定功能也并不难，但是如果你只根据JML规格写代码，比如你的Person用数组去存，你的qcs，qts，qlm等等都直接按照JML规格去一遍又一遍地遍历，结果就是你的程序会跑得像乌龟一样慢，然后强测大寄特寄。。。

所以说，掌握好规格与实现相分离的思想是至关重要的，也是我们必须去掌握的。所谓规格与实现分离是由规格和实现的不同要求决定的。规格的书写要求的是严谨、易读，所以在书写规格时要尽量使用简单的数据结构和最简单的算法，只要严谨地向程序员说明我们需要实现的功能即可。而实现则不仅要考虑程序的正确性，还要考虑程序能否高效地运行。这之间的鸿沟就需要我们去跨越，我们首先要理解JML规格中方法的具体作用，前提条件，限制条件等内容，然后再从一个程序员的角度分析应该采用何种数据结构去管理数据，应该使用何种算法去实现这样一个功能。这个过程也正是规格与实现相分离的过程。

从我自己的角度来看，我这几次作业开始的时候并没有很好地掌握规格与实现相分离的思想。做第一次作业的过程中，我初步的实现并没有去使用上文提到的动态维护和并查集思想，在判断isCircle、查询BlockSum和TripleSum的过程中都是直接采用了类似于JML规格中提供的遍历方式，导致程序的性能非常之差。后来，在同学的建议下我重新修改了程序的框架，采用了动态维护和并查集，使得程序效率提高了数倍，最终才得以顺利通过强测。

做第三次作业时就没有那么顺利了，这次作业中新增的指令要求我们能够查询该社交关系网络中的最小环路，对于这个问题大家都会想到先用dijkstra算法找到最短路径，之后再用其他算法找出最小环路。在寻找最小环路的过程中，我最开始想到的是遍历源点的每一个邻接点，删除这条直接边获得一个新图，然后在新图中使用dijkstra寻找两点间的最短路径，加上原来直连边的大小就得到了一个环路的长度。但是这样一来显然要跑很多次dijkstra算法，简直是噩梦。于是在同学的提示下想到了一种只用跑一遍dijkstra算法，然后直接去遍历边来获得最小环路的方法。具体思路就是遍历图中每一条边，如果源点到这条边的两个端点的最短路径不重合（对于一个端点是源点的边，要求源点到另一个端点的最短路径非直连），那么这个环路就可以作为一个备选的最小环路。如此一来就可以将时间复杂度控制在O(n)以内。

但是，但是，本以为这样就万事大吉了。可是强测总是出人意料。。。。

虽然寻找最小环的时间复杂度降低了，但是dijkstra的时间复杂度依旧很高，在图是稀疏图的情况下可能达到O(n^2)，这就严重影响了程序的效率，而强测也正好卡住了这个点，导致强测中出现了一个CPU_TIME_LIMIT_EXCEED。反思这个问题之后，我对dijkstra算法采用了堆优化，将每次更新完的最短路径都推入堆中，然后下一次只需要取堆顶元素进行递归即可，这样又将时间复杂度降低到了O((n+m)logn)左右，使得程序的效率提高了一倍左右。

由此可见，正确掌握规格与实现相分离的思想对于提高程序性能是至关重要的。

## 四、OKtest方法分析

本单元的每次作业都增加了一个方法对应的OKtest方法，主要有boolean型和int型两种，其中boolean型只需要判断是否满足规格，int型则还需要判断第一条不满足的规格是哪一条。

总体来说，OKtest方法是检验代码实现是否符合规格要求的一个有力手段，可以对规格的要求进行全覆盖的检验，并且还可以准确地给出不满足规格的位置，有利于快速定位并且修正bug。通过OKtest方法，我们不仅可以体会到JML规格对代码实现严格的约束作用，还可以感受到JML规格在debug时提供的巨大帮助。

本单元的OKtest方法主要是我们通过标准输入给出自己拟定的方法执行前后的状态，然后调用OKtest方法进行判断，这样虽然能够很好地检验OKtest方法的正确性，但是却忽略了它在我们编写其他方法时的作用，如果可以在运行其他方法时也调用OKtest方法，应该可以更好地发挥其真实价值。

## 五、学习体会

回顾本单元的学习，我们可以看到本单元主要训练我们根据规格实现代码并开展测试的能力。与前面两个单元注重算法的高效性和创新型不同，这一单元重点关注的是算法的严谨性与鲁棒性。

从JML规格书写的角度来看，我们需要保证自己足够严谨，能够正确给出方法的前置条件，副作用和后置条件，保证条件正确无遗漏、无异议。在必要的时候要构造中间数据，采用**共性提取机制**和**组合机制**等去优化规格，提高规格的可读性。

从根据JML规格实现代码的角度来看，我们需要了解规格管理了哪些数据，对哪些数据进行了操作，其本质作用是什么。在了解这些内容之后，我们需要跳脱规格，择优选择数据结构和算法，然后实现规格要求的功能。

在代码实现之后，我们还需要回到规格中去，根据规格的要求构造测试数据对我们的代码正确性和性能进行进一步的检测。

总而言之，经过这一单元的学习，我对一个基于JML规格的项目的开发流程基本熟悉，学会了阅读和书写较为严谨的JML规格，同时也掌握了阅读规格实现代码并且进行正确性验证的能力，最重要的是掌握了规格和实现分离的关键思想，这些对我以后的学习工作都将有重要的作用。
