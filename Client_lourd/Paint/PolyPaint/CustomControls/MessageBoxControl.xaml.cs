using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using Quobject.SocketIoClientDotNet.Client;
using Newtonsoft.Json;
using System.Runtime.CompilerServices;
using System;
using PolyPaint.Modeles;
using PolyPaint.Utilitaires;
using System.Collections.Generic;

namespace PolyPaint.CustomControls
{

    public partial class MessageBoxControl : UserControl, INotifyPropertyChanged
    {
        User user = User.instance;
        private AppSocket socket;
        private string _username;
        private string currentChannel;
        private List<string> channels = new List<string>();
        public event PropertyChangedEventHandler PropertyChanged;
        private Dictionary<string,string> messagesPerChannel = new Dictionary<string, string>();

        public MessageBoxControl()
        {
            DataContext = this;
            InitializeComponent();
            _username = user.Username;
            socket = AppSocket.Instance;
            GetChatRooms();
            currentChannel = "Général";
            socket.Emit("joinChannel", user.Username, "Général");

            socket.On("newChannel", (data) =>
            {
                GetChatRooms();
            });

            socket.On("chat message", (data) =>
            {
                Console.WriteLine("message!");
                Newtonsoft.Json.Linq.JObject obj = (Newtonsoft.Json.Linq.JObject)data;
                Newtonsoft.Json.Linq.JToken un = obj.GetValue("username");
                Newtonsoft.Json.Linq.JToken ts = obj.GetValue("timestamp");
                Newtonsoft.Json.Linq.JToken ms = obj.GetValue("message");
                Newtonsoft.Json.Linq.JToken channelName = obj.GetValue("channel"); 
                string newMessage = Environment.NewLine + un.ToString() + ts.ToString() + ":\n" + ms.ToString() + Environment.NewLine;
                updateDictionnary(newMessage.ToString(), channelName.ToString());
                Dispatcher.Invoke(() =>
                    {
                        if (currentChannel == channelName.ToString())
                        {
                            messageList.Text += newMessage.ToString();
                        }
                    
                    });
                
            });
        }

        private void updateDictionnary(string newMessage, string channel)
        {
            if (messagesPerChannel.ContainsKey(channel))
            {
                messagesPerChannel[channel] += newMessage;
            }
            else messagesPerChannel.Add(channel, newMessage);

            Console.WriteLine(newMessage);
        }
        

        public static readonly DependencyProperty ValueProperty =
        DependencyProperty.Register("Value", typeof(string), typeof(MessageBoxControl), new PropertyMetadata(null));

       
        public string Value
        {
            get { return (string)GetValue(ValueProperty); }
            set { SetValue(ValueProperty, value); }
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

        private void sendMessage(object sender, RoutedEventArgs e)
        {
            if (!string.IsNullOrWhiteSpace(MessageTextBox.Text))
            {
                socket.Emit("chat message", _username, ChatName.Text, MessageTextBox.Text);
            }
            MessageTextBox.Text = string.Empty;
            MessageTextBox.Focus();
        }

        private void sendMessageOnEnter(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
            {
                sendMessage(sender, e);
            }
        }

        private void AddChatRoom(object sender, RoutedEventArgs e)
        {
            NewChannelPopup.IsOpen = false;
            if (!string.IsNullOrWhiteSpace(newChannelName.Text)){

                socket.Emit("joinChannel", user.Username, newChannelName.Text);
                currentChannel = newChannelName.Text;
                ChatName.Text = (newChannelName.Text).ToString();
                messageList.Text = "";
                if (messagesPerChannel.ContainsKey(currentChannel))
                {
                    messageList.Text = messagesPerChannel[currentChannel].ToString();
                }
            }
            newChannelName.Text = "";
            
            if(currentChannel != "Général")
            {
                QuitterCanal.Visibility = Visibility.Visible;
            }else { QuitterCanal.Visibility = Visibility.Visible; }
        }

        private void GetChatRooms()
        {
            List<string> temp = new List<string>();
            socket.Emit("channels");
            socket.On("channels", (data) =>
            {
                temp = (JsonConvert.DeserializeObject<List<string>>(data.ToString()));
                Dispatcher.Invoke(() =>
                {
                    foreach (var channel in temp)
                {
                       
                        if(!channels.Contains(channel)) {
                            channels.Add(channel);
                            ListOfChannels.Items.Add(channel);
                        }
                    }
                });
            });
            
        }

        public void DisplayPopup(object sender, RoutedEventArgs e)
        {
            NewChannelPopup.IsOpen = true;
        }

        public void HidePopup(object sender, RoutedEventArgs e)
        {
            NewChannelPopup.IsOpen = false;
            newChannelName.Text = "";
        }

        private void messageList_TextChanged(object sender, TextChangedEventArgs e)
        {
            messageList.ScrollToEnd();
        }

        private void MessageTextBox_TextChanged(object sender, TextChangedEventArgs e)
        {

        }

         protected void OnPropertyChanged([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }

        private void ComboBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            socket.Emit("joinChannel", user.Username, ListOfChannels.SelectedItem);
            currentChannel = ListOfChannels.SelectedItem.ToString();
            ChatName.Text = (ListOfChannels.SelectedItem).ToString();
            messageList.Text = "";
            if (messagesPerChannel.ContainsKey(currentChannel))
            {
                messageList.Text = messagesPerChannel[currentChannel].ToString();
            }
            if (currentChannel != "Général")
            {
                QuitterCanal.Visibility = Visibility.Visible;
            }
            else { QuitterCanal.Visibility = Visibility.Visible; }

        }
    }
}
