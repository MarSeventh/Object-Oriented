# OO第二单元总结

## 目录

[toc]

## 一、前言

第二单元中，我们实现了以Java多线程为基础的电梯系统，首先通过学习Java多线程的用法以及线程安全问题的基本解决方法，实现最基本的电梯功能，然后通过更复杂的线程同步和互斥机制来适应更多新增的功能，同时不断优化电梯的调度策略来提高电梯的性能。总体而言，~~这一单元的任务其实并不复杂~~，总结下来就是处理好线程的同步和互斥问题并且设计更好的电梯调度策略。下面，我就对这一单元的三次作业做一个简单的总结，主要分析三次作业中线程安全的解决方案、线程间的交互关系以及电梯的调度策略。

## 二、第一次作业

第一次作业只用我们实现最基本的电梯功能，在学会使用同步块之后实现还是比较容易的。

> 该次作业的UML类图如下：
>
> ![hw5](https://alist.sanyue.site/d/imgbed/202311252251133.png)
>
> UML协作图如下：
>
> ![hw5seq](https://alist.sanyue.site/d/imgbed/202311252251074.png)

### 1、同步块的设置和锁的选择

本单元作业我采用的均是生产者-消费者模型，生产者即为输入线程Input，消费者是六个Elevator线程，具体流程是Input先将得到的乘客请求输入到公共等待队列waitmap中，然后通过调度器Manager线程将waitmap中的乘客分配给每个Elevator各自的等待队列waitlist中。

第一次作业中，我们只需要考虑input写入waitmap与manager读取waitmap的冲突以及manager写入电梯的waitlist和电梯的读取自身的waitlist的冲突即可，所以我直接给WaitList类的读写方法加了synchronized控制块，同时，在某些需要多次访问WaitList类的地方加上了synchronized控制块。

WaitList的读写方法如下：

```java
    public synchronized void addPerson(Person person) {
        waitList.add(person);
        notifyAll();
    }

    public synchronized Person getOnePerson() {
        if (waitList.isEmpty() && !isEnd) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (waitList.isEmpty()) {
            return null;
        }
        Person person = waitList.get(0);
        waitList.remove(0);
        notifyAll();
        return person;
    }
```

可以看到，为了保证同步块尽可能小，但是又要保证线程安全，所以要使锁和同步块中处理的语句密切相关，例如waitmap锁住的代码块里面要尽量都是读写waitmap的语句。

### 2、调度器及调度策略

本次作业我的调度器即为Manager类，它通过waitmap和Input与Elevator进行交互，Input向waitmap中写入数据，Manager从中取出，然后按照调度策略分配给相应电梯的waitlist。

这次作业我采用的调度策略也较为简单，为了充分利用电梯的空间，我采取的策略是如果请求乘客的目标方向与某一电梯的运行方向相同，并且该电梯还未到达请求乘客的出发层，且它的内部乘客和等待乘客之和不满6人，则将该电梯视作一个备选电梯，在所有备选电梯中选择一个离请求乘客最近的电梯作为最终决定的电梯。如果所有电梯都不满足备选电梯的条件，那么就按照乘客序号（非输入编号）模6的方式进行分配。

具体的实现如下：

```java
public WaitList getWaitList(Person person) {
        int des = person.getDes();
        int start = person.getStart();
        int distance = 20;
        WaitList waitList = null;
        for (Elevator elevator : elevators) {
            int dir = elevator.getDirection();
            int pos = elevator.getPos();
            int innum = elevator.getInnum();
            WaitList waitList1 = elevator.getWaitList();
            //if satisfy the requests, set waitList1 to waitList
            }
        }
        if (waitList == null) {
            waitList = waitLists.get(sum % 6);
        }
        sum++;
        return waitList;
    }
}
```

在这次的调度策略中，我其实还有很多没有考虑到的细节问题，这一部分会在第三次作业的调度策略中详细介绍。

### 3、bug分析

本次作业在强测和互测中都没有出现bug。

## 三、第二次作业

本次作业在第一次的基础上增加了电梯的ADD指令和MAINTAIN指令，整体思路并不复杂，对于增加电梯的指令，只需要考虑新建一个线程，将其加入到电梯队列中便可以进行调度，对于MAINTAIN指令，同样只需要将电梯内及等待队列中所有的乘客重新加入到waitmap中进行分配，然后从电梯队列中移除该电梯即可。但是，思路虽然不难，实际实现起来却有着不小的难度，这主要是因为要使电梯控制电梯队列，就无形中将Manager类也变成了共享对象，增加了线程冲突的危险，为了更好地解决这一问题，我引入了读写锁`ReentrantReadWriteLock`来进行一部分同步控制。除了这个问题以外，由于电梯随时都有可能将自己的人放回到waitmap中，这就导致第一次作业通过Input的结束判断Manager和Elevator的结束出现了问题，应该通过判断所有的人都到达了目的地并且Input输入结束来判断Manager和Elevator是否应该结束进程。

> 本次作业的UML类图如下：
>
> ![hw6](https://alist.sanyue.site/d/imgbed/202311252251393.png)
>
> UML协作图如下：
>
> ![hw6seq](https://alist.sanyue.site/d/imgbed/202311252252358.png)

### 1、同步块及锁的选择

由于本次作业与上次作业在基本架构方面未发生改变，所以上次的同步控制块基本不用加以修改。主要修改的部分是对于Manager类和Input类的控制，由于ADD和Maintain指令需要修改Manager类的电梯队列，因此这个类被动成为共享对象，所以我在这个类中加上了读写锁来保证线程安全，具体实现如下：

```java
   //Manager
   public void addElevators(Elevator elevator) {
        lock.writeLock().lock();
        try {
            elevators.add(elevator);
            waitLists.add(elevator.getWaitList());
        } finally {
            lock.writeLock().unlock();
        }
    }
    public void removeElevators(int id) {
        lock.writeLock().lock();
        try {
            for (Elevator elevator : elevators) {
                if (elevator.getID() == id) {
                    waitLists.remove(elevator.getWaitList());
                    elevators.remove(elevator);
                    break;
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
```

除此之外，由于在Maintain之后需要修改被踢出乘客的出发地，因此Person也可能出现线程冲突，所以我在该类内部也加上了读写锁。

```java
    public void setStart(int newstart) {
        lock.writeLock().lock();
        try {
            start = newstart;
        } finally {
            lock.writeLock().unlock();
        }
    }
```

### 2、调度器及调度策略

本次作业由于对运送乘客的要求与第一次基本一致，所以调度策略基本没有改变，只是将没有备用电梯时模6变成了分配给内部和外部乘客数量之和最小的电梯，尽量保证了电梯的均衡性，避免由于Maintain使得每次都把乘客分给少数电梯的情况。但是这也导致我第一次没有考虑到的小细节依然没有考虑到（悲~），很多情况下运行效率较低。

### 3、bug分析

本次作业在强测和互测环节都没有出现bug，主要的问题是在写的过程中出现的。

第一个问题是没有考虑一开始就出现Maintain的情况，由于每个电梯在开始没有乘客输入的情况下，都会进入waitlist.wait()状态，这就导致一开始输入Maintain时电梯不能正确进入下一循环执行Maintain，发现这个问题后我在输入Maintain指令后对对应电梯的waitlist进行了notify，成功唤醒电梯，解决了这个问题。

第二个问题是各个线程何时结束的问题。由于第一次作业时电梯队列在Input结束后就已经设为了end，导致这一次Maintain的电梯放出去的人可能没有别的电梯可坐。为了解决这个问题，我也是下了一番功夫。既然不能在Input结束时给waitmap和waitlist置为end，那么应该在什么时机置位呢？这个问题其实不难，就是在Input输入结束，waitmap和每个电梯的waitlist均为空，并且电梯中已经没有乘客的情况下结束。由于本人思维水平有限，在解决这个问题时很久都没有想到好的方案，最后才茅塞顿开，其实可以在每个电梯运送完所有乘客后都唤醒一次Manager，让Manager判断一次是否可以结束程序。同时，在Input输入结束时也应该唤醒一次Manager，再判断是否可以结束。这样就可以保证无论Input何时结束，程序都可以在运送完所有乘客后正常结束，从而解决了这个棘手的问题~~（至少对我来说）~~。

## 四、第三次作业

先放图。

> UML类图如下：
>
> ![hw7](D:\OO\第二单元总结\hw7.png)
>
> UML协作图如下：
>
> ![hw7seq](https://alist.sanyue.site/d/imgbed/202311252252193.png)
>
> 可以看到，这次的UML协作图只比第二次新增了Elevator向Manager返回换乘的乘客这一步。

第三次作业加入的是乘客换乘以及在特定楼层停靠并且限制每层同时停靠数的功能。与第二次作业不同，这次作业主要需要我们在电梯的调度策略上进行修改。至于如何满足电梯在特定楼层停靠，只需要给每个电梯增加一个表示可停靠楼层的属性（用掩码表示），在停靠之前进行判断即可。然后对于限制每层同时停靠数的功能，我采用的是一个类似信号量的方法~~（我能说是我当时还不知道信号量这个东西吗？）~~，就是新建一个CountController对象，每个电梯开门前都需要向该对象申请，未达到停靠限制则允许停靠，否则自动陷入等待。这样，就满足了停靠限制的要求。接下来，最难的部分在于换乘路线的选择以及调度策略的修改。对于换乘路线的选择，我采用的是一种较为暴力的方法，即通过递归的方式求出所有的可行路线，然后由于电梯运行具有不确定性，所以我只考虑第一段乘坐的电梯，利用之前的调度方案选择一个相对较优的电梯乘坐。

### 1、同步块及锁的选择

前面提到，本次作业增加的内容主要是在调度策略上实现的，因此同步块及锁基本与上次保持一致，只是在CountController中注意严格的线程互斥即可，同时在修改调度策略时注意对waitmap和waitlist的保护。

### 2、调度器及调度策略

本次作业我对调度器的调度策略进行了大幅度的改动，首先是对于无换乘的情况，依然沿用之前的最近同向电梯的思路，但是考虑了更多的细节问题。

> 1、当电梯中没有人时，如果请求乘客和电梯运行方向相同，只有当该乘客上电梯和下电梯的楼层都在电梯目前位置和去接的乘客上电梯的位置之间时才可以捎带。
>
> 2、当电梯中没有人时，如果请求乘客和电梯运行方向相反，则看请求乘客目的方向是否和电梯去接的乘客方向相同，若相同，看该乘客上电梯的位置是否在电梯去接的乘客上电梯的位置之后。如果符合，则该电梯可以做备用电梯，但是距离等于电梯到它要接的人的距离加上它要接的人的距离到请求乘客上电梯位置的距离之和。

对于换乘，我的策略是除非迫不得已不进行换乘，即实在没有直达电梯时再考虑进行换乘，求所有换乘路径的算法是利用了Hashmap和ArrayList，将所有可以到达地路径存入一个ArrayList中，Hashmap存的是坐的每一步电梯id以及起始楼层和终止楼层。具体算法如下：

> ```java
>    public WaitList transSearch(Person person) {
>         lock.readLock().lock();
>         try {
>             ArrayList<HashMap<Elevator, Integer>> alllines = new ArrayList<>();
>             HashMap<Elevator, Integer> line = new HashMap<>();
>             searchAllLines(alllines, line, person.getStart(), person.getDes());
>             WaitList waitList;
>             waitList = transSearchFind(person, alllines);
>             return waitList;
>         } finally {
>             lock.readLock().unlock();
>         }
>     }
> ```
>
> 其中调用的`searchAllLines`即递归存储所有的路径，`transSearchFind`负责在找到的所有路径中按照旧策略寻找一条较优路径。

### 3、bug分析

本次作业的强测和互测都没有出现bug。

## 五、总结

通览这三次作业，我们可以看到其实整体的架构并没有发生太大的变化，从第一次到最后一次只增加了一个CountController类，由此可见，生产者-消费者模型的可扩展性是很强的，这也说明了层次化设计的重要性。通过这两次迭代，我们可以看出，如果要增加对电梯运行的限制，大多数情况下我们只需要修改Elevator中的内容即可，如果需要修改调度策略，只需要修改Manager中的内容即可，整体架构并不需要进行改动。这三次作业中，稳定的内容是电梯的运行方式，例如上行、下行、开门、关门、上下乘客等都没有发生较大改变，易变的内容就是电梯的运行限制条件以及电梯的调度策略。

本单元的一大难点就是如何解决线程安全问题，课堂上老师主要给我们介绍了wait、notify和读写锁`ReentrantReadWriteLock`两种解决方案。其实这两种解决方案在大多数情况下的作用基本相同，但是在某些情况下却只能使用特定的解决方案。例如，当我们在一个代码段中药多次重复访问一个共享对象时，任何一个时刻丢掉锁都可能导致程序的异常执行，这种情况下就必须使用synchronized进行代码块的修饰。所以说，这两者都是非常重要的，我们必须全部掌握，才能更好地解决线程安全的问题。

无论如何，传说中的电梯单元就已经结束了，虽然遇到了些许困难，但是经过一个月的洗礼，我又掌握了不少知识，对面向对象思想的理解更加透彻，同时对Java的使用也更加熟悉，希望接下来的OO学习过程中能够再接再厉！