## Hbase

------

### 读流程

> 数据可能存储在**BlockCache**，**memStore**以及**HFile**
>
> 读取数据的时候先从**BlockCache**中读取
>
> 如果**BlockCache**中没有，再视图从**memStore**中读取
>
> 如果**memStore**中没有，则从**HFile**读取数据，根据行键的范围以及布隆过滤器来筛选，筛选掉没有指定的**HFile**，剩余的**HFile**中不代表有这个数据

### 合并机制

> minor compaction
>
> > 将相邻的几个小的HFile合并成一个大的HFile
>
> major compaction
>
> > 将这个HStore中所有的HFile合并成一个
>
> 在默认情况下，HBase使用的是minor compaction

> 在合并过程中，数据会进行整体的排序，会舍弃掉一部分数据
>
> > 被删除的数据，超过版本的数据

