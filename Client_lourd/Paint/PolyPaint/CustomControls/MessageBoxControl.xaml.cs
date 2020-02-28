﻿using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using Quobject.SocketIoClientDotNet.Client;
using Newtonsoft.Json;
using System.Runtime.CompilerServices;
using System;
using PolyPaint.Modeles;

namespace PolyPaint.CustomControls
{

    public partial class MessageBoxControl : UserControl, INotifyPropertyChanged
    {
        private const string SERVER_IP = "127.0.0.1";
        private const string SERVER_PORT = "5050";
        private Socket socket;
        private string _serverIP;
        private string _username;
        public event PropertyChangedEventHandler PropertyChanged;
        Message message = new Message();

        public MessageBoxControl()
        {
            DataContext = this;
            InitializeComponent();
            _serverIP = SERVER_IP;
            _username = "Genevieve";
            socket = IO.Socket("http://" + _serverIP + ":" + SERVER_PORT);
            
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
                socket.Emit("chat message", _username, MessageTextBox.Text);
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
