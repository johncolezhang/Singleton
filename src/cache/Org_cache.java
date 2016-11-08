package cache;

import java.util.List;

import dao.OrgdataDao;
import entity.Org;

public class Org_cache {
	public static List<Org> orgs;
	public Org_cache(){
		orgs = OrgdataDao.searchKpi();
	}
}
