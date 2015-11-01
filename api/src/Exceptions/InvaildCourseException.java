package Exceptions;

public class InvaildCourseException extends Exception
{

    private static final long serialVersionUID = 1997753363232807011L;

		public InvaildCourseException()
		{
		}

		public InvaildCourseException(String message)
		{
			super(message);
		}

		public InvaildCourseException(Throwable cause)
		{
			super(cause);
		}

		public InvaildCourseException(String message, Throwable cause)
		{
			super(message, cause);
		}

		public InvaildCourseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
		{
			super(message, cause, enableSuppression, writableStackTrace);
		}
}
