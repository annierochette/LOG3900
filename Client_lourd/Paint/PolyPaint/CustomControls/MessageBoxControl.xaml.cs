﻿using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using Quobject.SocketIoClientDotNet.Client;
using Newtonsoft.Json;
using System.Runtime.CompilerServices;
using System;
using PolyPaint.Modeles;
using PolyPaint.Utilitaires;

namespace PolyPaint.CustomControls
{

    public partial class MessageBoxControl : UserControl, INotifyPropertyChanged
    {
        User user = User.instance;
        private AppSocket socket;
        private string _username;
        public event PropertyChangedEventHandler PropertyChanged;
       

        public MessageBoxControl()
        {
            DataContext = this;
            InitializeComponent();
            _username = user.Username;
            socket = AppSocket.Instance;
            
            socket.On("chat message", (data) =>
            {
                Newtonsoft.Json.Linq.JObject obj = (Newtonsoft.Json.Linq.JObject)data;
                Newtonsoft.Json.Linq.JToken un = obj.GetValue("username");
                Newtonsoft.Json.Linq.JToken ts = obj.GetValue("timestamp");
                Newtonsoft.Json.Linq.JToken ms = obj.GetValue("message");
           
               MessageList += Environment.NewLine + un.ToString() + ts.ToString() + ":\n" + ms.ToString() + Environment.NewLine;
                
            });
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
                socket.Emit("chat message", _username, "General", MessageTextBox.Text);
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
    }
}
