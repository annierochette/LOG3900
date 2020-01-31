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
        public MainWindow()
        {
            InitializeComponent();
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
                Console.WriteLine("yaaas");
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
            this.Dispatcher.Invoke(() =>
            {
                messageList.Items.Add(message);

            });
        }
       
        
        private void sendMessage(object sender, RoutedEventArgs e)
        {
            string message = MessageBox.Text;
            socket.Emit( "chat message", JsonConvert.SerializeObject(message));
           
        }
    }
}
