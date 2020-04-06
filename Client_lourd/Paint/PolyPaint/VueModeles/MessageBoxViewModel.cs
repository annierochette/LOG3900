using System;
using System.ComponentModel;
using System.Runtime.CompilerServices;


namespace PolyPaint.VueModeles
{
    class MessageBoxViewModel : INotifyPropertyChanged, IPageViewModel
    {
        public string _messageList;
        public event PropertyChangedEventHandler PropertyChanged;

        public string MessageList
        {
            get
            {
                return _messageList;
            }
            set
            {
                Console.WriteLine(value);
                _messageList = value;
                OnPropertyChanged("MessageList");
            }
        }


        protected void OnPropertyChanged([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}
