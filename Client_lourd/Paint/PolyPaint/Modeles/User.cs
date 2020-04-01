using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;

namespace PolyPaint.Modeles
{
    class User : INotifyPropertyChanged
    {
        private string username;

        public string Username
        {
            get { return username; }
            set
            {
                username = value;
                ChangeProperty();
            }
        }

        public event PropertyChangedEventHandler PropertyChanged;


        protected void ChangeProperty([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }

}
