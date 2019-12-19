
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FrameDemo extends JFrame implements ActionListener {

	JPanel panel;
	JLabel user_label, password_label, message;
	JTextField userName_text;
	JPasswordField password_text;
	JButton submit, cancel;

	FrameDemo() {

		// User Label
		user_label = new JLabel();
		user_label.setText("User Name :");
		userName_text = new JTextField();

		// Password

		password_label = new JLabel();
		password_label.setText("Password :");
		password_text = new JPasswordField();

		// Submit

		submit = new JButton("SUBMIT");

		panel = new JPanel(new GridLayout(3, 1));

		panel.add(user_label);
		panel.add(userName_text);
		panel.add(password_label);
		panel.add(password_text);

		message = new JLabel();
		panel.add(message);
		panel.add(submit);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Adding the listeners to components..
		submit.addActionListener(this);
		add(panel, BorderLayout.CENTER);
		setTitle("Please Login Here !");
		setSize(300, 100);
		setVisible(true);

	}

	public static void main(String[] args) {
		new FrameDemo();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		System.out.println("Performing login........");
		String userName = userName_text.getText();
		String password = password_text.getText();

		URL url = null;
		try {
			url = new URL("http://localhost:8080/api/authenticate/" + userName + "/" + password);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			String response = "";
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				response += output;
			}
			System.out.println("Server result");
			System.out.println(response);
			conn.disconnect();

			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting();
			Gson gson = builder.create();
			AuthenticateResponse authenticateReponse = gson.fromJson(response, AuthenticateResponse.class);
			System.out.println(authenticateReponse);

			message.setText(" Login exitoso para usuario: " + userName + "");

			String tokenLoginUrl = "http://localhost:8080/token-login/"+authenticateReponse.getAuthToken();
			java.awt.Desktop.getDesktop().browse(java.net.URI.create(tokenLoginUrl));
		} catch (Exception e) {
			System.out.println(e);
			message.setText(" Usuario invalido..");
		}

	}

}
