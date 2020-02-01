using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Quobject.SocketIoClientDotNet.Client;
using Newtonsoft.Json;

namespace Prototype_Lourd
{

    public partial class MainWindow : Window
    {
        public bool isConnected { get; set; }
        public MainWindow()
        {
            InitializeComponent();
            isConnected = false;
            socket.On("chat message", (data) =>
            {
                addMessage((string)data);
            });
        }

        private const string SERVER_IP= "127.0.0.1"; 
        private const string SERVER_PORT = "8080";
  
        private Socket socket = IO.Socket("http://" + SERVER_IP + ":" + SERVER_PORT);

        private void connectToIPAddress(object sender, RoutedEventArgs e)
        {
            socket = IO.Socket("http://" + SERVER_IP + ":" + SERVER_PORT);
            socket.On(Socket.EVENT_CONNECT, () =>
            {
                Console.WriteLine("Connected to server");
                isConnected = true;
            });

            socket.On(Socket.EVENT_CONNECT_TIMEOUT, () =>
            {
                System.Windows.MessageBox.Show("Connection timeout", "Error");
            });
            socket.On(Socket.EVENT_CONNECT_ERROR, () =>
            {
                System.Windows.MessageBox.Show("Connection error", "Error");
            });

        }

  
        private void addMessage(string message)
        {
            Dispatcher.Invoke(() =>
            {
                messageList.Items.Add(message);
 
            });
        }

        private void OnKeyDownHandler(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
            {
                sendMessage(sender, e);
            }
        }

        private void sendMessage(object sender, RoutedEventArgs e)
        {
            if (!string.IsNullOrWhiteSpace(messageBox.Text)) { 
            string timestamp = getTimestamp(DateTime.Now);
            string message = timestamp + " "+ messageBox.Text;
            socket.Emit( "chat message", JsonConvert.SerializeObject(message));
            }
            messageBox.Text = string.Empty;
        }

        public static string getTimestamp(DateTime value)
        {
            return value.ToString("HH:mm:ss");
        }

        private void messageBox_TextChanged(object sender, TextChangedEventArgs e)
        {

        }

        
    }
}
