using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Windows.Ink;

namespace Prototype_Lourd.Model
{
   
    class UserConnection : INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;
        

        private string username = "";
        private string ipAddress = "127.0.0.1";
    
        public string Username
        {
            get { return username; }
            set { username = value; ModifiedProperty(); }
        }

        public string IPAddress
        {
            get { return ipAddress; }
            set { ipAddress = value; ModifiedProperty(); }
        }

        public bool IsConnected { get; set; } = false;
       

        protected void ModifiedProperty([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }

        public void ChooseUsername(string username) => Username = username;
        public void SelectIpAddress(string ipAddress) => IPAddress = ipAddress;
        public void ChangeConnectionStatus(bool isConnected) => IsConnected = isConnected;


    }
}