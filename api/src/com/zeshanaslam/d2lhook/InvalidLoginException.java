package com.zeshanaslam.d2lhook;

public class InvalidLoginException extends Exception
{

    private static final long serialVersionUID = 1997753363232807009L;

		public InvalidLoginException()
		{
		}

		public InvalidLoginException(String message)
		{
			super(message);
		}

		public InvalidLoginException(Throwable cause)
		{
			super(cause);
		}

		public InvalidLoginException(String message, Throwable cause)
		{
			super(message, cause);
		}

		public InvalidLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
		{
			super(message, cause, enableSuppression, writableStackTrace);
		}
}
