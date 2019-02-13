package cn.nsg;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * 模拟银通支付网关给商户系统返回异步通知参数
 */
@WebServlet("/CallbackResp")
public class CallbackResp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CallbackResp() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "http://127.0.0.1:8088/payment/servlet/PaymentResponse?orderid=1548677051309&opstate=0&ovalue=1.00&sysorderid=B5214780958325411457&completiontime&attach&msg&sign=68106663a21ff1450301b030268d31fa";
		String str = PaymentUtil.request(url);
		System.out.println("商户客户端返回的状态代码: " + str);
		if("opstate=0".equals(str)) {
			System.out.println("商户已经成功收到回调信息 ");
		}else {
			//如果商户没有成功收到回调信息, 则每隔1分钟重新发送, 连续发送5次
			for(int i=0;i<5;i++) {
				try {
					Thread.sleep(1000*60);
					str = PaymentUtil.request(url);
					if("opstate=0".equals(str)) {
						System.out.println("商户已经成功收到回调信息 ");
						break;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.err.println("商户回调通知失败 ");
		}
		/**
		 * 如果用下面这种重定向的方式给商户服务器返回参数, 商户服务器获取请求参数后可以解析并将数据存入request中然后转发到jsp页面显示, 如果用上面的方式将请求参数返回给商户
		 * 服务器, 则商户处理该请求的servlet只能解析参数后, 给网关服务器返回数据, 本地不能执行页面转发操作.
		 */
//		response.sendRedirect("http://127.0.0.1:8088/payment/servlet/PaymentResponse?orderid=1548677051309&opstate=0&ovalue=1.00&sysorderid=B5214780958325411457&completiontime&attach&msg&sign=68106663a21ff1450301b030268d31fa");
		
		//发送完异步通知后接着发送同步通知, 由于同步通知和异步通知请求的servlet不一样, 所以在商户服务器上两个可以同时执行.
		response.sendRedirect("http://127.0.0.1:8088/payment/servlet/HrefResponse?o=B4667033958566694253&uo=1550021459497&c=992&t=101&v=1.00&e=%u6210%u529f&u=1616&s=2");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
