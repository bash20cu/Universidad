   M S B A M L   `   `   `  ���� K  GServices_Control, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null3clr-namespace:Services_ControlServices_Control  !    Services_Control.MainWindow   W SPresentationCore, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35e aSystem.Windows.Controls.Ribbon, Version=6.0.2.0, Culture=neutral, PublicKeyToken=b77a5c561934e089R NWindowsBase, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35F 9http://schemas.microsoft.com/winfx/2006/xaml/presentation     R NSystem.Xaml, Version=6.0.2.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     @d2http://schemas.microsoft.com/expression/blend/2008     Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     0localclr-namespace:Services_Control       � Title$  Control de servicios��$	��449��$	��538�� � 
ResizeMode$ NoResize=� � 	Resources  	  Boton �� 
TargetType �� Property �� Value �� 
TargetType ��Name �� CornerRadius	 �� Triggers
 P� Property P� Value �� 
TargetName��%�  &	        �� " ��.���  � ��# � ����  � ��$ 1q���  � }�$
 True����  � ��$ 20����  � �� �� " ��.R��� $ ButtonBorder��#��z��#��z��#��z��$ 6,6,6,6��._��� $��Center=�$��Center=�	 P� 
 �@J�IsMouseOver$
 True��. ��� $ ButtonBorder�� � �� �#6495EDP� 
 � ��$
 True��. ��� $ ButtonBorder�� � ��
 ��  ��� $ ButtonBorder�� � ��$ 2q�.��� $��	0,10,0,-6q�$
��Left=�$	��528�� � ColumnDefinitions �� .+��� -    ��Name$ Texto�� �� TextWrapping$
 Wrap=�$��TextBox��$��
-1,126,3,5q����	#FF2F2D2D���	#FFF3F0F0$ �� HorizontalScrollBarVisibility$
 Auto=�" �� VerticalScrollBarVisibility$
 Auto=� �� 
IsReadOnly$
 True��$��14(�$	��526���� -    ��Name$ mysql��$��MySQL��$��22,56,365,0q�$	��Top=�$��17��
��������� -   $ mssql��$��MSSQL��$
��Left=�$��
114,56,0,0q�$	��Top=�$��17��$	��112���� -    ��Name$ btnShutdownServices��$��Apagar Servicios��$
��Left=�$��	18,11,0,0q�$	��Top=�$	��115��#��� ��$��1,1,1,1q�$
}�True��$��34��#��[  #��� ��#��� �� J� OpacityMask# � ���� -   $ btnEncenderServicios��$��Encender Servicios��$
��Left=�$��
142,11,0,0q�$	��Top=�$	��115��$
}�True��#��� ��$��34��#��[  #��� ��#��� ��# � ��� -    �Name$ ProgressBar��$��Center=�$��19��$��0,92,0,0q�$	��Top=�$	��494��