using PolyPaint.Utilitaires;
using System.Windows.Input;


namespace PolyPaint.VueModeles
{
    public class GameModeMenuViewModel : BaseViewModel, IPageViewModel
    {
        private ICommand _goToDrawingWindow;

        public ICommand GoToDrawingWindow
        {
            get
            {
                return _goToDrawingWindow ?? (_goToDrawingWindow = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToDrawingWindow", "");
                }));
            }
        }

    }
}