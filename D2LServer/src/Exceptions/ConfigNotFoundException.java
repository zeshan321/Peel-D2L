package Exceptions;

public class ConfigNotFoundException extends Exception
{

    private static final long serialVersionUID = 1997753363232807011L;

		public ConfigNotFoundException()
		{
		}

		public ConfigNotFoundException(String message)
		{
			super(message);
		}

		public ConfigNotFoundException(Throwable cause)
		{
			super(cause);
		}

		public ConfigNotFoundException(String message, Throwable cause)
		{
			super(message, cause);
		}

		public ConfigNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
		{
			super(message, cause, enableSuppression, writableStackTrace);
		}
}