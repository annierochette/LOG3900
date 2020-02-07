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
        private string _serverIP;
        private string _username;
        public event PropertyChangedEventHandler PropertyChanged;
        public MainWindow()
        {
            DataContext = this;
            InitializeComponent();
            _serverIP = SERVER_IP;
            _isConnected = false;
            _hasValidUsername = false;
            socket = IO.Socket("http://" + ipTextBox.Text + ":" + SERVER_PORT);
            socket.On("chat message", (data) =>
            {
               // Message messageTemplate = new Message();
               // Console.WriteLine(data);
               // var message = JsonConvert.DeserializeAnonymousType(data.ToString(), messageTemplate);
                
                string timeStamp = getTimestamp(DateTime.Now);
                //MessageList += Environment.NewLine + timeStamp + " " + message.username + ": " + message.body + Environment.NewLine;
                MessageList += Environment.NewLine + timeStamp + " "+ (string)data + Environment.NewLine;
            });
       
        }

        private void connectToIPAddress(object sender, RoutedEventArgs e)
        {

            if (!string.IsNullOrWhiteSpace(ipTextBox.Text)) {

            socket = IO.Socket("http://" + _serverIP + ":" + SERVER_PORT);
            socket.On(Socket.EVENT_CONNECT, () =>
            {
                IsConnected = true;
                
            });

            socket.On(Socket.EVENT_CONNECT_ERROR, () =>
            {
                //System.Windows.MessageBox.Show("Erreur de connexion.", "Erreur");
                Console.WriteLine("Connection failed");
                 
            });
            }
            
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
        private string _messageList = "";
        public string MessageList
        {
            get
            {
                return _messageList;
            }
            set
            {
                _messageList = value;
                OnPropertyChanged("MessageList");
            }
        }

        private void connectToIPAdressOnEnter(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
            {
                connectToIPAddress(sender, e);
            }
        }

        private void sendMessageOnEnter(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
            {
                sendMessage(sender, e);
            }
        }

        private void selectUsernameOnEnter(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
            {
                selectUsername(sender, e);
            }
        }

        private void sendMessage(object sender, RoutedEventArgs e)
        {
            if (!string.IsNullOrWhiteSpace(messageBox.Text)) { 
            //string timestamp = getTimestamp(DateTime.Now);
            //    Message message = new Message();
            //    message.username = _username;
            //    message.body=  messageBox.Text;
                socket.Emit( "chat message", JsonConvert.SerializeObject(messageBox.Text));
            }
            messageBox.Text = string.Empty;
            messageBox.Focus();
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

            if (!string.IsNullOrWhiteSpace(usernameTextBox.Text))
            {
                
                _username = usernameTextBox.Text;
                socket.Emit("changeUsername", (_username));
                socket.On("changeUsername", (data) =>
                {
                    if (JsonConvert.SerializeObject(data) == "true")
                    {
                        HasValidUsername = true;
                    }
                    else { System.Windows.MessageBox.Show("Désolé, ce pseudonyme est déjà pris!", "Erreur"); }
                });

            }
         
        }

        private void ipTextBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            _serverIP = ipTextBox.Text;
        }

        protected void OnPropertyChanged([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }

        private void disconnectButton_Click(object sender, RoutedEventArgs e)
        {
            socket.Emit("disconnection", _username);
            IsConnected = false;
            HasValidUsername = false;
            usernameTextBox.Text = string.Empty;
            messageList.Text = string.Empty;

           
        }

        private void messageList_TextChanged(object sender, TextChangedEventArgs e)
        {
            messageList.ScrollToEnd();
        }

       
    }
}
