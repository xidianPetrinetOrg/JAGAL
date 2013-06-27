package graph.algorithm.coloring;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;


public class Coloring<U extends Object> {
	
	private final String colorAssigmentFormat = "%s: %s";
	
	private Map<U, Integer> coloring = new HashMap<U, Integer>();
	private Multiset<Integer> colorSupport = new Multiset<Integer>();
	
	public Coloring(){}
	
	public Integer getColor(U key) throws ParameterException{
		Validate.notNull(key);
		return coloring.get(key);
	}
	
	public void setColor(U key, int color) throws ParameterException{
		Validate.notNull(key);
		if(isColored(key))
			colorSupport.decMultiplicity(coloring.get(key));
		coloring.put(key, color);
		colorSupport.incMultiplicity(color);
	}
	
	public void uncolor(U key) throws ParameterException{
		Validate.notNull(key);
		coloring.remove(key);
	}
	
	public boolean isColored(U key) throws ParameterException{
		Validate.notNull(key);
		return coloring.containsKey(key);
	}
	
	public int chromaticNumber(){
		return colorSupport.support().size();
	}
	
	public boolean isComplete(Collection<U> keys){
		return coloring.keySet().containsAll(keys);
	}
	
	public Map<Integer,Set<U>> getColorGroups(){
		Map<Integer,Set<U>> colorGroups = new HashMap<Integer, Set<U>>();
		for(U key: coloring.keySet()){
			if(!colorGroups.containsKey(coloring.get(key))){
				colorGroups.put(coloring.get(key), new HashSet<U>());
			}
			colorGroups.get(coloring.get(key)).add(key);
		}
		return colorGroups;
	}
	
	public Set<U> getUncoloredKeys(Collection<U> keys) throws ParameterException{
		Validate.notNull(keys);
		Set<U> result = new HashSet<U>();
		if(keys.isEmpty())
			return result;
		for(U key: keys){
			if(!isColored(key)){
				result.add(key);
			}
		}
		return result;
	}
	
	public Coloring<U> clone(){
		Coloring<U> clone = new Coloring<U>();
		for(U key: coloring.keySet()){
			try {
				clone.setColor(key, coloring.get(key));
			} catch (ParameterException e) {
				// Cannot happen, since all keys are taken from the keyset.
				e.printStackTrace();
			}
		}
		return clone;
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("GraphColoring {");
		builder.append('\n');
		for(U key: coloring.keySet()){
			builder.append(String.format(colorAssigmentFormat, key, coloring.get(key)));
			builder.append('\n');
		}
		builder.append('}');
		builder.append('\n');
		return builder.toString();
	}

}
