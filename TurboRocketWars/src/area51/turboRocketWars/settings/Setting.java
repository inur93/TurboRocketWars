package area51.turboRocketWars.settings;

import area51.turboRocketWars.exceptions.SettingTypeException;


public class Setting {

	private String value;
	private String defValue;
	private String newValue = null;
	private String regEx;
	private final String name;

	public Setting(String name, Boolean value, String regEx){
		this(name, value.toString(), regEx);
	}
	
	public Setting(String name, Double value, String regEx){
		this(name, value.toString(), regEx);
	}

	public Setting(String name, Float value, String regEx){
		this(name, value.toString(), regEx);
	}

	public Setting(String name, Integer value, String regEx){
		this(name, value.toString(), regEx);
	}

	public Setting(String name, Long value, String regEx){
		this(name, value.toString(), regEx);
	}

	public Setting(String name, String value, String regEx) {
		this.name = name;
		this.value = value;
		this.defValue = value;
		this.regEx = regEx;
	}

	public Class<?> getType(){
		return getClass();
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(Class<T> type) throws SettingTypeException{
		if(type.equals(Integer.class))	return (T) Integer.valueOf(value);
		if(type.equals(Float.class)) return (T) Float.valueOf(value);
		if(type.equals(Long.class)) return (T) Long.valueOf(value);
		if(type.equals(String.class)) return (T) String.valueOf(value);
		if(type.equals(Double.class)) return (T) Double.valueOf(value);
		if(type.equals(Boolean.class)) return (T) Boolean.valueOf(value);
		throw new SettingTypeException(type.toGenericString());
	}

	public void setValue(String value){
		if(value.matches(regEx) || regEx == null){
			try{
				this.newValue = value;
			}catch(Exception e){
				System.err.println("failed cast to: " + value.getClass());
			}
		}else{
			System.err.println("value does not match.\n"
					+ "value: " + value + "\n"
					+ "regex: " + regEx);
		}
	}

	public void save(){
		if(newValue != null) value = newValue;
	}

	public void cancel(){
		newValue = null;
	}

	public String getName(){
		return this.name;
	}

	public void reset(){
		this.value = defValue;
	}

}
