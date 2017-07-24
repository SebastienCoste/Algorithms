package fr.sttc.problems.flow;

public class NamedEdge extends Edge {
	
	public String roadName;

	public NamedEdge (Integer capacity, City source, City destination) {
		super(capacity, source, destination);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((roadName == null) ? 0 : roadName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NamedEdge other = (NamedEdge) obj;
		if (roadName == null) {
			if (other.roadName != null)
				return false;
		} else if (!roadName.equals(other.roadName))
			return false;
		return true;
	}
	
}
