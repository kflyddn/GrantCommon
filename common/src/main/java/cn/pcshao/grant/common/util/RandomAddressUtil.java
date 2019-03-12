package cn.pcshao.grant.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author pcshao.cn
 * @date 2019-03-12
 */
public class RandomAddressUtil {

    private static int l1 = 95;
    private static int l2 = 79;
    private static int l3 = 79;
    private static int l4 = 79;
    private List<String> p1 = new ArrayList<>();
    private List<String> p2 = new ArrayList<>();
    private List<String> p3 = new ArrayList<>();
    private List<String> p4 = new ArrayList<>();

    {
        p1.add( "北京市");
        p1.add( "天津市");
        p1.add( "上海市");
        p1.add( "重庆市");
        p1.add( "河北省");
        p1.add( "山西省");
        p1.add( "辽宁省");
        p1.add( "吉林省");
        p1.add( "黑龙江省");
        p1.add( "江苏省");
        p1.add( "浙江省");
        p1.add( "安徽省");
        p1.add( "福建省");
        p1.add( "江西省");
        p1.add( "山东省");
        p1.add( "河南省");
        p1.add( "湖北省");
        p1.add( "湖南省");
        p1.add( "广东省");
        p1.add( "海南省");
        p1.add( "四川省");
        p1.add( "贵州省");
        p1.add( "云南省");
        p1.add( "陕西省");
        p1.add( "甘肃省");
        p1.add( "青海省");
        p1.add( "台湾省");
        p1.add( "台湾省内蒙古自治区");
        p1.add( "西藏自治区");
        p1.add( "宁夏回族自治区");
        p1.add( "新疆维吾尔自治区");
        p1.add( "香港特别行政区");
        p1.add( "澳门特别行政区");
    }

    public static void main(String[] args) {
        RandomAddressUtil addressUtil = new RandomAddressUtil();
        for (int i = 0; i < 5000; i++) {
            System.out.println(addressUtil.getP1());
        }
    }

    private String getP1() {
        Random random = new Random();
        int i = random.nextInt(p1.size()-1);
        return p1.get(i);
    }
}
