package elasticjob;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * ElasticJob 定时任务处理类
 */
public class ArchiveJob implements SimpleJob {
    /**
     * 定时任务执行逻辑
     *
     * @param shardingContext
     */
    @Override
    public void execute(ShardingContext shardingContext) {

        test1(shardingContext);


    }

    private void test1(ShardingContext shardingContext) {
        int shardingItem = shardingContext.getShardingItem();
        System.out.println(">>>>>>>当前分片"+ shardingItem);
        //获取分片参数
        String shardingParameter = shardingContext.getShardingParameter();
        //1 查询未归档
        String select = "select * from resume where state='未归档' and education='"+shardingParameter+"' limit 1";
        List<Map<String, Object>> list = JdbcUtil.executeQuery(select);

        if (list == null || list.size() == 0) {
            System.out.println("数据已全部处理");
            return;
        }

        //2 修该为 已归档
        Map<String, Object> data = list.get(0);
        Object name = data.get("name");
        Object id = data.get("id");
        Object education = data.get("education");
        System.out.println("" + id +"--"+ name  +"--"+education);

        System.out.println("===>updating.....");
        String update = "update resume set state='已归档' where id=?";
        JdbcUtil.executeUpdate(update, id);

        //3 插入备份、更新原数据
        String insert = "insert into resume_backup select * from resume where id=?";
        JdbcUtil.executeUpdate(insert, id);
    }

}
