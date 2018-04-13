package cn.com.larunda.monitor.gson;

import java.util.List;

/**
 * Created by sddt on 18-3-26.
 */

public class MapInfo {

    /**
     * max : 3808.65
     * data : [{"name":"昆山秧浦机械制造有限公司","lng":"121.006352","lat":"31.373762","count":3808.65,"percent":100,"data":{"water":74.45,"power":5.44,"steam":0.53,"gas":3728.23,"total":3808.65},"original_data":{"power":16477.96,"water":868.75,"steam":4151.26,"gas":2803.18},"top_ten":1},{"name":"昆山市康菲热流道系统有限公司","lng":"120.900779","lat":"31.295794","count":458.56,"percent":100,"data":{"water":75.02,"power":8.44,"steam":0.05,"gas":375.05,"total":458.56},"original_data":{"power":25573.46,"water":875.43,"steam":414.09,"gas":281.99},"top_ten":2},{"name":"昆山市张浦花叶凹凸印刷厂","lng":"120.972067","lat":"31.227061","count":456.6,"percent":100,"data":{"water":75.18,"power":4.37,"steam":0.05,"gas":377,"total":456.6},"original_data":{"power":13244,"water":877.25,"steam":417.08,"gas":283.46},"top_ten":3},{"name":"苏州金帝科机械设备有限公司","lng":"121.012611","lat":"31.416604","count":455.83,"percent":100,"data":{"water":73.77,"power":6.39,"steam":0.05,"gas":375.62,"total":455.83},"original_data":{"power":19368.74,"water":860.79,"steam":408.6,"gas":282.42},"top_ten":4},{"name":"昆山荣欣旺机电有限公司","lng":"121.042317","lat":"31.372096","count":454.01,"percent":100,"data":{"water":74.95,"power":3.82,"steam":0.05,"gas":375.19,"total":454.01},"original_data":{"power":11575.14,"water":874.56,"steam":420.42,"gas":282.1},"top_ten":5},{"name":"昆山鼎宏五金精密模具有限公司","lng":"121.002387","lat":"31.414727","count":452.03,"percent":100,"data":{"water":74.7,"power":6.4,"steam":0.05,"gas":370.88,"total":452.03},"original_data":{"power":19405.3,"water":871.63,"steam":415.05,"gas":278.86},"top_ten":6},{"name":"昆山法欧特自动化设备有限公司","lng":"120.896733","lat":"31.273314","count":451.52,"percent":100,"data":{"water":74.61,"power":4.26,"steam":0.05,"gas":372.6,"total":451.52},"original_data":{"power":12908.9,"water":870.64,"steam":419.02,"gas":280.15},"top_ten":7},{"name":"昆山市港浦污水处理有限公司","lng":"120.972225","lat":"31.227067","count":451.17,"percent":100,"data":{"water":73.59,"power":6.21,"steam":0.05,"gas":371.32,"total":451.17},"original_data":{"power":18823.72,"water":858.68,"steam":422.08,"gas":279.19},"top_ten":8},{"name":"昆山衡泽盛精密设备有限公司","lng":"120.969557","lat":"31.310238","count":451.1,"percent":100,"data":{"water":74.25,"power":2.5,"steam":0.05,"gas":374.3,"total":451.1},"original_data":{"power":7566.07,"water":866.42,"steam":418.68,"gas":281.43},"top_ten":9},{"name":"昆山市全九京金属热处理有限公司","lng":"120.947609","lat":"31.317596","count":449.89,"percent":100,"data":{"water":74.57,"power":1.47,"steam":0.05,"gas":373.8,"total":449.89},"original_data":{"power":4455.56,"water":870.13,"steam":414.81,"gas":281.05},"top_ten":10},{"name":"昆山本广颖电线有限公司","lng":"121.131293","lat":"31.409032","count":443.06,"percent":100,"data":{"water":74.23,"power":1.25,"steam":0.05,"gas":367.53,"total":443.06},"original_data":{"power":3773.5,"water":866.19,"steam":425.85,"gas":276.34},"top_ten":0}]
     */

    private double max;
    private List<DataBeanX> data;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public List<DataBeanX> getData() {
        return data;
    }

    public void setData(List<DataBeanX> data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * name : 昆山秧浦机械制造有限公司
         * lng : 121.006352
         * lat : 31.373762
         * count : 3808.65
         * percent : 100
         * data : {"water":74.45,"power":5.44,"steam":0.53,"gas":3728.23,"total":3808.65}
         * original_data : {"power":16477.96,"water":868.75,"steam":4151.26,"gas":2803.18}
         * top_ten : 1
         */

        private String name;
        private String lng;
        private String lat;
        private double count;
        //private double percent;
        private DataBean data;
        private OriginalDataBean original_data;
        private int top_ten;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }

        /*public double getPercent() {
            return percent;
        }

        public void setPercent(double percent) {
            this.percent = percent;
        }*/

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public OriginalDataBean getOriginal_data() {
            return original_data;
        }

        public void setOriginal_data(OriginalDataBean original_data) {
            this.original_data = original_data;
        }

        public int getTop_ten() {
            return top_ten;
        }

        public void setTop_ten(int top_ten) {
            this.top_ten = top_ten;
        }

        public static class DataBean {
            /**
             * water : 74.45
             * power : 5.44
             * steam : 0.53
             * gas : 3728.23
             * total : 3808.65
             */

            private double water;
            private double power;
            private double steam;
            private double gas;
            private double total;

            public double getWater() {
                return water;
            }

            public void setWater(double water) {
                this.water = water;
            }

            public double getPower() {
                return power;
            }

            public void setPower(double power) {
                this.power = power;
            }

            public double getSteam() {
                return steam;
            }

            public void setSteam(double steam) {
                this.steam = steam;
            }

            public double getGas() {
                return gas;
            }

            public void setGas(double gas) {
                this.gas = gas;
            }

            public double getTotal() {
                return total;
            }

            public void setTotal(double total) {
                this.total = total;
            }
        }

        public static class OriginalDataBean {
            /**
             * power : 16477.96
             * water : 868.75
             * steam : 4151.26
             * gas : 2803.18
             */

            private double power;
            private double water;
            private double steam;
            private double gas;

            public double getPower() {
                return power;
            }

            public void setPower(double power) {
                this.power = power;
            }

            public double getWater() {
                return water;
            }

            public void setWater(double water) {
                this.water = water;
            }

            public double getSteam() {
                return steam;
            }

            public void setSteam(double steam) {
                this.steam = steam;
            }

            public double getGas() {
                return gas;
            }

            public void setGas(double gas) {
                this.gas = gas;
            }
        }
    }
}
