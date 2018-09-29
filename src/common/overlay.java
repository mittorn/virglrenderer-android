package common;
import android.content.*;
import android.graphics.*;
import android.view.*;
import android.app.*;
import android.util.Log;
import android.os.Handler;

public class overlay {
	private static final String T = "virgl-java";
	private static native void nativeRun(int fd);
	private static native int nativeAccept(int fd);
	private static native int nativeOpen();
	private static native int nativeInit(String settings)
	private static native void nativeUnlink();
	private static Handler handler;
	private static Context ctx;
    private static WindowManager wm;
	
	private static void start_next(int svc_id)
	{
		java.util.List<ActivityManager.RunningServiceInfo> services = 
		((ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE)).
		getRunningServices(Integer.MAX_VALUE);
		for(int i = 1; i < 32; i++)
		{
			boolean free = true;
			
			if( i == svc_id )
				continue;
			for(ActivityManager.RunningServiceInfo s :services)
			{
		
				if(s.service.getClassName().equals("process.p"+i))
				{
					free = false;
					break;
				}
			}
			if(free)
			{
				Log.d(T,"starting instance "+i);
				ctx.startService( new Intent().setClassName(ctx, "process.p"+i));
				return;
			}
		}
	}

	private static void run_mt()
	{

		new Thread(){
			@Override
			public void run()
			{
				int sock = nativeOpen();
				if( sock < 0 )
				{
					Log.d(T, "Failed to open socket!");
					ctx.stopService( new Intent().setClassName(ctx, "process.p1"));
					return;
				}
				int fd;
				while((fd = nativeAccept(sock)) >= 0)
				{
					final int fd1 = fd;
					Thread t = new Thread(){
						@Override
						public void run()
						{
							nativeRun(fd1);
						}
					};
					t.start();
				}
			}
		}.start();
	}
	private static void run_mp(final int svc_id)
	{
		new Thread()
		{
			public void run()
			{
				int fd = nativeOpen();
				if( fd < 0 )
				{
					Log.d(T, "Failed to open socket!");
					ctx.stopService( new Intent().setClassName(ctx, "process.p"+svc_id));
					return;
				}
				fd = nativeAccept(fd);
				nativeUnlink();
				start_next(svc_id);
				nativeRun(fd);
				ctx.stopService( new Intent().setClassName(ctx, "process.p"+svc_id));
			}
		}.start();
	}
	
	public static void start(Context ctx1, int svc_id)
	{
		ctx = ctx1;
		wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		byte[] settings = new byte[65];
		System.loadLibrary("vtest");
		int thread_mode = nativeInit(ctx.getFilesDir().getPath()+"/settings");
		handler = new Handler();
		if( thread_mode == 1 )
			run_mt();
		else
			run_mp(svc_id);
	}

    private static SurfaceView create(final int x, final int y, final int width, final int height) {
	    //resize(x,y,width, height);
		final Thread t = Thread.currentThread();
		final SurfaceView surf[] = new SurfaceView[1];
		try
		{
			Log.d(T, "post");

			handler.postDelayed(new Runnable(){
			public void run()
			{
				surf[0] = new SurfaceView(ctx);
				WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, PixelFormat.OPAQUE);
				params.gravity = Gravity.LEFT | Gravity.TOP;
				params.x = x;
				params.y = y;
				params.width = width;
				params.height = height;
				if( (width == 0) || (height == 0) )
				{
					params.width = params.height = 32;
				}
				wm.addView(surf[0], params);
				Log.d(T, "notify");
				synchronized(t)
				{
				t.notify();
				}
			}
		},100);
		synchronized(t)
		{
		t.wait();
		t.sleep(1000);
		}
		Log.d(T, "resume");
		}
		catch(Exception e)
		{
			e.printStackTrace();

			Log.d(T, "int");
			//return null;
		}
		return surf[0];
    }
	
	private static void set_rect(final SurfaceView surface, final int x, final int y, final int width, final int height, final int visible)
	{

		Log.d(T,"resize " + x + " " + y + " " + width );
		handler.post(new Runnable()
		{
			public void run()
			{
				try
				{
					WindowManager.LayoutParams params = (WindowManager.LayoutParams)surface.getLayoutParams();
		
					if( params == null )
						return;
					if( visible != 0 )
					{
						params.x = x;
						params.y = y;
						params.width = width;
						params.height = height;
					}
					else
					{
						params.x = params.y = -33;
						params.width = params.height = 32;
					}
					wm.updateViewLayout(surface, params);
				}
				catch(Exception e)
				{}
			}
		});
	}

    public static void destroy(final SurfaceView surface) {
		handler.post(new Runnable(){
			public void run()
			{
				wm.removeView(surface);
			}
		});
    }
	public static Surface get_surface(SurfaceView surf)
	{
		return surf.getHolder().getSurface();
	}
}
