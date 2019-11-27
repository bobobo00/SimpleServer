package server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebContext {
	private List<Entity> entitys;
	private List<Mapping> mappings;
	//key-name  value-clz
	Map<String,String> entityMap=new HashMap<>();
	//key-pattern  value-name
	Map<String,String> mappingMap=new HashMap<>();
	
	public WebContext(List<Entity> entitys, List<Mapping> mappings) {
		this.entitys = entitys;
		this.mappings = mappings;
		//将entity 的List转成了对应map
		for(Entity entity:entitys) {
			entityMap.put(entity.getName(), entity.getClz());
		}
		//将mapping的List转成了对应map
		for(Mapping mapping:mappings) {
			for(String pattern:mapping.getPatterns()) {
				mappingMap.put(pattern, mapping.getName());
			}
		}
	}
	/**
	 * 通过URL的路径找到了对应class
	 * @param pattern
	 * @return
	 */
	public String getClz(String pattern) {
		String name=mappingMap.get(pattern);
		return entityMap.get(name);
	}
	
}
