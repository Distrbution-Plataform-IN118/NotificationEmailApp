import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ResourceBundle.Control;

public class SubscribeNotificationEmail implements Runnable {
	private static final String EXCHANGE_NAME = "notification";
	public final static int TOTAL_SUBSCRITOS = 3;
	public int id_subscritos = 0;
	public String current_email; 
	//public static String list_email[] = new String[TOTAL_SUBSCRITOS];
	
	
	public static String list_email[] = {"jcrbsa@gmail.com","richardsonbruno@gmail.com","rbsa2@cin.ufpe.br"};

	public SubscribeNotificationEmail(int id, String email) {
		// TODO Auto-generated constructor stub
		id_subscritos = id;
		current_email = email;
	}
	public static Email controlador_email;
	public static void main(String[] argv) throws Exception {
		controlador_email = new Email();
		int cont_subscritos = 0;
		while (cont_subscritos < TOTAL_SUBSCRITOS) {
			new Thread(new SubscribeNotificationEmail(cont_subscritos, list_email[cont_subscritos])).start();
			cont_subscritos++;
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);//Exchange
			String queueName = channel.queueDeclare().getQueue();//Declaro fila aleatorio 
			channel.queueBind(queueName, EXCHANGE_NAME, "");//Realiz o bind com a Exchange->Fila->Consumidor

			System.out
					.println(" [*] Esperando Mensagens [" + id_subscritos + "]");

			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag,
						Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					System.out.println(" [x] Recebido '[" +id_subscritos + "] "+  message + "'");
					try {
						System.out.println(" [x] Email Enviado '[" +id_subscritos + "] "+  message + "'");
						controlador_email.send(message, current_email);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
			};
			channel.basicConsume(queueName, true, consumer);
		} catch (Exception e) {

		}
	}
	
	
}
