package util;

import java.util.ArrayList;
import java.util.List;

import com.jcraft.jsch.KeyPairDSA;

import cache.Kpi_cache;

public class BuildTable {
	static int [][]mdcommon=
		{
			{1,31},{2,28},{3,31},{4,30},{5,31},{6,30},
			{7,31},{8,31},{9,30},{10,31},{11,30},{12,31}
		};
	static int [][]mdleap=
		{
			{1,31},{2,29},{3,31},{4,30},{5,31},{6,30},
			{7,31},{8,31},{9,30},{10,31},{11,30},{12,31}
		};
	
	public static void main(String[] args){
		//Kpi_cache cache = new Kpi_cache();
		//List<String> time1 = getTime("year","1.2016","2.2017");
		//List<String> time2 = getTime("month","3.2016.2","4.2017.8");
		System.out.println(getTime("day","20151107","20151207"));
		
		
		//List<String> brand = getBrandOrArea("102,");
		/*for(int i = 0;i < time1.size();i++){
			System.out.println(time1.get(i));
		}
		for(int i = 0;i < time3.size();i++){
			System.out.println(time3.get(i));
		}*/
		
		/*for(int i = 0;i < brand.size();i++){
			System.out.println(brand.get(i));
		}*/
		/*String []a = getTime("day", "20161011");
		for(String s:a){
			System.out.println(s);
		}*/
		//System.out.println(selectMonth(2, 2016));
		//getGapday(2016, 6, 9, 2016, 8, 9);
	}
	

	
	public static List<String> getBrandOrArea(String brand){//格式：1,2,3,4,
		List<String> brands = new ArrayList<String>();
		String[] a = null;
		if(brand!=null){
			a = brand.split("\\,");
		}
		for(int i = 0;i<a.length;i++){
			for(int j = 0;j<Kpi_cache.kpis.size();j++){
				if(Kpi_cache.kpis.get(j).getDim_value() == a[i])	//指标值id与传入id相符
				{					
					brands.add( Kpi_cache.kpis.get(j).getDim_value());
				}
			}
		}
		return brands;
	}
	
	public static String[] getTime(String frequent,String time){
		String[] times = null;
		if(frequent.equals("year")){
			times = new String[1];
			times[0] = time.substring(0,4);
		}else if(frequent.equals("month")){
			times = new String[13];
			int year = Integer.parseInt(time.substring(0,4))-1;//输入日期的前一年
			//System.out.println(year);
			int month = Integer.parseInt(time.substring(4,6));
			//System.out.println(month);
			for(int i = 0;i<13;i++){
				if(month+i>12){
					year++;
					month -= 12;
				}
				if(month+i>=1 && month+i<=9){
					times[i] = year+"0"+(month+i);
				}else{
					times[i] = year+""+(month+i);
				}
			}
		}if(frequent.equals("day")){
			times = new String[31];//顺序的时间
			String [] ctimes = new String[31];//逆序的时间
			int year = Integer.parseInt(time.substring(0,4));//输入日期的前一年
			//System.out.println(year);
			int month = Integer.parseInt(time.substring(4,6));
			//System.out.println(month);
			int day = Integer.parseInt(time.substring(6,8));
			//times[0] = time;
			for(int i = 0;i<31 ; i++){
				//day--;
				if(day<=0){
					month--;
					if(month<=0){
						month = 12;
						year--;
					}
					day = selectMonth(month, year);
				}
				if(month<=9 &&day<=9){
					ctimes[i] = year+"0"+month+"0"+day;
				}else if(month<=9 &&day>9){
					ctimes[i] = year+"0"+month+""+day;
				}else if(month>9 &&day<=9){
					ctimes[i] = year+""+month+"0"+day;
				}else{
					ctimes[i] = year+""+month+""+day;
				}
				day--;
			}
			for(int i =0;i<31;i++){
				times[i] = ctimes[30-i];
			}
		}
		return times;
	}
	
	public static int selectMonth(int month,int year){
		boolean isleap = false;
		if((year%100==0&&year%400==0)||(year%100!=0&&year%4==0)){
			isleap = true;
		}
		if(isleap == false){
			return mdcommon[month-1][1];
		}else{
			return mdleap[month-1][1];
		}
	}
	
	public static String getTime(String frequent,String begintime,String endtime){//格式:1.2.3
		List<String> time = new ArrayList<String>();
		String times ="";
		if(frequent.equals("year")){
			int beginyear = Integer.parseInt(begintime.substring(0,4));
			int endyear = Integer.parseInt(endtime.substring(0,4));
			int gap = endyear - beginyear;
			for(int i = 0;i<gap+1;i++){
				time.add((beginyear+i)+"");
				times += (beginyear+i)+",";
			}
		}else if(frequent.equals("month")){
			int beginyear = Integer.parseInt(begintime.substring(0,4));
			int beginmonth = Integer.parseInt(begintime.substring(4,6));
			int endyear = Integer.parseInt(endtime.substring(0,4));
			int endmonth = Integer.parseInt(endtime.substring(4,6));
			int yeargap = endyear - beginyear;//年间隔
			int monthgap =  endmonth - beginmonth;//月间隔
			if(yeargap == 0){//同一年的情况
				for(int i = 0;i < monthgap+1;i++){
					if(beginmonth+i<10){
						time.add(beginyear+"0"+(beginmonth+i));
						times += beginyear+"0"+(beginmonth+i)+",";
					}else{
						time.add(beginyear+""+(beginmonth+i));
						times += beginyear+""+(beginmonth+i)+",";
					}
				}
			}else if(yeargap > 0){//不同年的情况
				monthgap = yeargap*12+(endmonth-beginmonth);//月间隔
				int count=0;//计数器，每满12加1
				for(int i = 0;i<monthgap+1;i++){
					if((beginmonth + i - count*12) <= 12){//没有跨时间的情况
						if(beginmonth+i- 12*count<10){
							time.add(beginyear +"0" + (beginmonth+i- 12*count));
							times += beginyear +"0" + (beginmonth+i- 12*count)+",";
						}else{
							time.add(beginyear +"" + (beginmonth+i- 12*count));
							times += beginyear +"" + (beginmonth+i- 12*count)+",";
						}
					}else if(beginmonth+i-count*12 > 12){
						count++;
						beginyear++;
						if(beginmonth+i- 12*count<10){
							time.add(beginyear +"0" + (beginmonth+i- 12*count));
							times += beginyear +"0" + (beginmonth+i- 12*count)+",";
						}else{
							time.add(beginyear +"" + (beginmonth+i- 12*count));
							times += beginyear +"" + (beginmonth+i- 12*count)+",";
						}
					}
				}
			}	
		}else if(frequent.equals("day")){
			int day = 0;//计数器，记录总共有几天
			int beginyear = Integer.parseInt(begintime.substring(0,4));
			int beginmonth = Integer.parseInt(begintime.substring(4,6));
			int beginday = Integer.parseInt(begintime.substring(6,8));
			
			int endyear = Integer.parseInt(endtime.substring(0,4));
			int endmonth = Integer.parseInt(endtime.substring(4,6));
			int endday = Integer.parseInt(endtime.substring(6,8));
						
			int currentmonth = beginmonth;//当前执行程序值
			int currentyear = beginyear;
			int currentday = beginday;
			day = getGapday(beginyear, beginmonth, beginday, endyear, endmonth, endday);//获取两时间之间的间隔天数
			currentday = endday;//最后的时间赋给程序当前时间
			currentmonth = endmonth;
			currentyear = endyear;
			for(int i = 0;i<day; i++){
				if(currentday<=0){
					currentmonth--;
					if(currentmonth<=0){
						currentmonth = 12;
						currentyear--;
					}
					currentday = selectMonth(currentmonth, currentyear);
				}
				if(currentmonth<=9 &&currentday<=9){
					time.add(currentyear+"0"+currentmonth+"0"+currentday);
				}else if(currentmonth<=9 &&currentday>9){
					time.add(currentyear+"0"+currentmonth+""+currentday);
				}else if(currentmonth>9 &&currentday<=9){
					time.add(currentyear+""+currentmonth+"0"+currentday);
				}else{
					time.add(currentyear+""+currentmonth+""+currentday);
				}
				currentday--;
			}
			
			String[] strtime1 = new String[time.size()];
			time.toArray(strtime1);
			for(int i =0;i<time.size();i++){
				times += strtime1[time.size()-i-1]+",";
			}
			
		}
		return times;
	}
	
	static int getGapday(int beginyear,int beginmonth,int beginday,
			int endyear,int endmonth,int endday){
		int day =0;
		int yeargap = endyear - beginyear;//年间隔
		int monthgap =  endmonth - beginmonth;//月间隔
		
		int currentmonth = beginmonth;//当前执行程序值
		int currentyear = beginyear;
		int currentday = beginday;
		if(yeargap == 0){
			for(int i = 0;i< monthgap;i++){//头n-1个月的天数
				day += selectMonth(currentmonth,currentyear)-currentday+1;//例，1号到31号有31天所以要加1
				currentmonth++;
				currentday=1;
			}
			day += (endday+1)-currentday;//最后一个月的天数 
		}else if(yeargap > 0){
			monthgap = yeargap*12+(endmonth-beginmonth);
			for(int i = 0;i<monthgap;i++){//头n-1个月的天数
				if(currentmonth >12){//超过12则减12
					currentmonth -= 12;
					currentyear++;
				}
				day +=selectMonth(currentmonth,currentyear)-currentday+1;
				currentmonth++;
				currentday=1;
			}
			day += (endday+1)-currentday;//7到5应该有3天，所以endday要加1
		}
		System.out.println(day);
		return day;
	}
}
