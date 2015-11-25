package domain;

public class NewsCategory {
	private String id;
	private String name;
	
	public NewsCategory(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public NewsCategory() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
