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
using System.ComponentModel;
using System.Runtime.CompilerServices;

namespace Prototype_Lourd
{

    public partial class MainWindow : INotifyPropertyChanged
    {
        private const string SERVER_IP = "127.0.0.1";
        private const string SERVER_PORT = "8080";
        private Socket socket;
        private bool _isConnected;
        private bool _hasValidUsername;
        private string serverIP;
        private string username;
        public event PropertyChangedEventHandler PropertyChanged;
        public MainWindow()
        {
            DataContext = this;
            InitializeComponent();
            serverIP = SERVER_IP;
            _isConnected = false;
            _hasValidUsername = false;
            socket = IO.Socket("http://" + serverIP + ":" + SERVER_PORT);
            socket.On("chat message", (data) =>
            {
                addMessage((string)data);
            });
        }

       

        private void connectToIPAddress(object sender, RoutedEventArgs e)
        {
            socket = IO.Socket("http://" + ipTextBox.Text + ":" + SERVER_PORT);
            socket.On(Socket.EVENT_CONNECT, () =>
            {
                Console.WriteLine("Connected to server");
                IsConnected = true;
                
            });

            //socket.On(Socket.EVENT_CONNECT_TIMEOUT, () =>
            //{
            //    System.Windows.MessageBox.Show("Délai de connexion dépassé", "Error");
                
            //});
         
            //socket.On(Socket.EVENT_CONNECT_ERROR, () =>
            //{
            //    System.Windows.MessageBox.Show("Erreur de connexion", "Error");
                
            //});

        }

        public bool IsConnected 
        {
            get {
                return _isConnected; }
            set
            {
                _isConnected = value; 
                OnPropertyChanged("IsConnected"); 
            }
        }

        public bool HasValidUsername
        {
            get
            {
                return _hasValidUsername;
            }
            set
            {
                _hasValidUsername = value;
                OnPropertyChanged("HasValidUsername");
            }
        }

        private void addMessage(string message)
        {
            Dispatcher.Invoke(() =>
            {
                messageList.Items.Add(message);
 
            });
        }

        private void sendMessageOnEnter(object sender, KeyEventArgs e)
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
            string message = timestamp + " "+ username +": "+ messageBox.Text;
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

        private void selectUsername(object sender, RoutedEventArgs e)
        {
            username = usernameTextBox.Text;
            HasValidUsername = true;
            usernameTextBox.Text = string.Empty;
        }

        private void ipTextBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            serverIP = ipTextBox.Text;
        }

        protected void OnPropertyChanged([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }

        private void disconnectButton_Click(object sender, RoutedEventArgs e)
        {
            IsConnected = false;
        }
    }
}
