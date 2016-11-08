package cache;

import java.util.List;

import dao.DirdataDao;
import entity.Dir;

public class Dir_cache {
	public static List<Dir> dirs;
	public Dir_cache(){
		dirs = DirdataDao.searchKpi();
	}

}
