package fr.sttc.problems.flow;

public class City {

	@Override
	public String toString() {
		return "City [type=" + type + ", name=" + name + ", capacity=" + capacity + "]";
	}
	public Type type;
	public String name;
	public Integer capacity;
	
	public City(Type type, String name, Integer capacity) {
		this.type = type;
		this.capacity = capacity;
		this.name = name;
	}
	
	public enum Type {
		SOURCE,
		TRANSIT,
		DESTINATION;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((capacity == null) ? 0 : capacity.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		if (capacity == null) {
			if (other.capacity != null)
				return false;
		} else if (!capacity.equals(other.capacity))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	
}
