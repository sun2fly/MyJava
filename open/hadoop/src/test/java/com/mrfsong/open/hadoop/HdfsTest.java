package com.mrfsong.open.hadoop;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.FileInputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: songfei20
 * @Date: 2020/12/16 18:41
 * @Description:
 */
public class HdfsTest {

    private static final String DEFAULT_FS = "hdfs://hadoop1:9000";

    public static void main(String[] args) {
        Configuration conf = new Configuration();

        /*//这里指定使用的是 hdfs文件系统
        conf.set("fs.defaultFS", "hdfs://hadoop1:9001");

        //通过这种方式设置java客户端身份
        System.setProperty("HADOOP_USER_NAME", "hadoop");*/

        try(FileSystem fs = FileSystem.get(new URI("hdfs://hadoop1:9000"),conf,"hadoop")){

            fs.mkdirs(new Path("/hello"));//创建一个目录

            FSDataOutputStream outputStream = fs.create(new Path("/hello/1.txt"), true);
            FileInputStream inputStream = new FileInputStream("/Users/felix/data/github/flinkx/flinkx-test/src/main/resources/stream_to_hdfs.json"); //从本地输入流。
            IOUtils.copy(inputStream, outputStream); //完成从本地上传文件到hdfs

            //上传本地文件到HDFS
            fs.copyFromLocalFile(new Path("/Users/felix/data/github/flinkx/flinkx-test/src/main/resources/stream_to_hdfs.json"),new Path("/hello/2.txt"));

            //下载HDFS文件到本地
            fs.copyToLocalFile(new Path("/hello/2.txt"),new Path("/tmp/hello.txt"));

            //获取文件详情
            RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/test/flinkx/checkpoint"), true);

            while(listFiles.hasNext()){
                LocatedFileStatus status = listFiles.next();

                // 输出详情
                System.out.println("File Status : " + status.toString());

                // 获取存储的块信息
                BlockLocation[] blockLocations = status.getBlockLocations();

                for (BlockLocation blockLocation : blockLocations) {
                    System.out.println("BlockLocation : " + blockLocation.toString());
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void testMultiWriter () throws Exception {

        Configuration conf = new Configuration();
        String file = "/Users/felix/Downloads/HDF_3.1.1_virtualbox_180626.ova";
        java.nio.file.Path srcFilePath = Paths.get(file);
        long sizeInBytes = Files.size(srcFilePath);
        String displaySize = FileUtils.byteCountToDisplaySize(sizeInBytes);
        Runnable task = () -> {
            try(FileSystem fs2 = FileSystem.get(new URI("hdfs://hadoop1:9001"),conf,"hadoop")){
                FSDataOutputStream outputStream = fs2.create(new Path("/hello/1.txt"), true);
                FileInputStream inputStream = new FileInputStream("/Users/felix/Downloads/HDF_3.1.1_virtualbox_180626.ova"); //从本地输入流。
                IOUtils.copy(inputStream, outputStream); //完成从本地上传文件到hdfs
            }catch (Exception e) {
                e.printStackTrace();
            }

        };

        long startTs = System.currentTimeMillis();

        //模拟多线程写入同一个HDFS文件
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(String.format("upload file : %s , size : %s , cost : %d seconds" , file ,displaySize,(System.currentTimeMillis() - startTs) / 1000));


    }



    private void getLatestChkPath () {
        Path chkHdfsPath = new Path("/test/flinkx/checkpoint");
        Configuration conf = new Configuration();

        /*//这里指定使用的是 hdfs文件系统
        conf.set("fs.defaultFS", "hdfs://hadoop1:9001");

        //通过这种方式设置java客户端身份
        System.setProperty("HADOOP_USER_NAME", "hadoop");*/

        try (FileSystem fs = FileSystem.get(new URI("hdfs://hadoop1:9001"), conf, "hadoop")) {
            if (fs.exists(chkHdfsPath)) {
                FileStatus[] fileStatuses = fs.listStatus(new Path("/test/flinkx/checkpoint"));
                List<FileStatus> latestJobDir = Arrays.stream(fileStatuses)
                        .sorted((o1, o2) -> (int) (o2.getModificationTime() - o1.getModificationTime()))
//                        .limit(3) // 此参数应该支持可配置、默认应该和flink#num-retained参数保持一致
                        .collect(Collectors.toList());

                findChk:
                for (FileStatus status : latestJobDir) {
                    List<FileStatus> chkStatusDir = Arrays.stream(fs.listStatus(status.getPath(), p -> p.getName().startsWith("chk-")))
                            .sorted((o1, o2) -> (int) (o2.getModificationTime() - o1.getModificationTime()))
                            .collect(Collectors.toList());

                    /*checkpoint目录校验 */
                    for (FileStatus chkStatus : chkStatusDir) {
                        RemoteIterator<LocatedFileStatus> remoteIterator = fs.listFiles(chkStatus.getPath(), true);
                        //判断chk目录是否为空、必要文件是否存在
                        if (remoteIterator.hasNext()) {
                            System.out.println("find checkpoint dir : " + chkStatus.getPath().toString());
                            break findChk;
                        }
                    }

                }
            }
        } catch (Exception e) {

        }
    }
}