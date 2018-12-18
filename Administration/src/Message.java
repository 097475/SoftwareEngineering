import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



public class Message {
private List<String[]> data;
private String type;

@SuppressWarnings("unchecked")
public Message(String type, List<?> src){
	this.data = new ArrayList<>();
	this.type = type == null? "" : type;
	if(src == null || src.isEmpty()) return;
	else if(src.get(0) instanceof String) {
		src = src.stream().map(x -> new String[]{(String) x}).collect(Collectors.toList());
	}
	else if(!(src.get(0) instanceof String[]))
		return;
			
	for(String[] elem : (List<String[]>)src) {
		this.data.add(elem);
	}
}
public Message(String str){
	this.data = new ArrayList<>();
	this.data.add(new String[] {str});
	this.type = "";
}


public List<String[]> getData() {
	return data;
}

public String getType() {
	return type;
}

public void printMessage()
{
	System.out.print(this.type);
	this.data.stream().forEach(x->Arrays.stream(x).forEach(System.out::print));
	System.out.println();
}

}
