using PolyPaint.Utilitaires;
using System.Windows.Input;

namespace PolyPaint.VueModeles
{
    class GuessingViewModel : BaseViewModel, IPageViewModel
    {
        public GuessingViewModel()
        {
            ActivateDrawing = false;
        }

        private bool _drawing;
        public void assignDrawingView()
        {
            Mediator.Notify("GoToFreeForAll", "");
        }

        public override string GetCurrentViewModelName()
        {
            return "GuessingViewModel";
        }

        private ICommand _goToGameModeMenu;
        public ICommand GoToGameModeMenu
        {
            get
            {
                return _goToGameModeMenu ?? (_goToGameModeMenu = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToGameModeMenu", "");
                }));
            }
        }

        public bool ActivateDrawing
        {
            get { return _drawing; }
            private set
            {
                _drawing= value;
                
            }
        }
    }
}

