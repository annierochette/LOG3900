using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PolyPaint.Modeles
{
    public class Message : INotifyPropertyChanged
    {
        private string _messageList;


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

        
        public event PropertyChangedEventHandler PropertyChanged;
        protected void OnPropertyChanged(string propertyName)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
   
    }
}


