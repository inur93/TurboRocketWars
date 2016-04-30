package area51.turboRocketWars.exceptions;

public class SettingTypeException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SettingTypeException(String type) {
		super("Type was not recognized: " + type);
		// TODO Auto-generated constructor stub
	}

	public SettingTypeException(Throwable cause) {
		super(cause);
	}

	public SettingTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public SettingTypeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
