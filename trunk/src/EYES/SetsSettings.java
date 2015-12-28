package EYES;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

/**
 * Сериализуемый класс, 
 * Форма позволяет настраивать различные напоминания, упражнения для глаз
 * Функция конструктор initSetsSettings
 * Создаёт ряд компонентов для указания контрольных точек остановки движения глаз
 */
@SuppressWarnings({"serial" })
public class SetsSettings implements java.io.Serializable {
	/**
	 * Вспомогательный класс для описания конкретной опорной точки.
	 * Данный класс используется как основа для списка точек в упражнении
	 */
	public class Property implements java.io.Serializable {
		// Класс для сохранения номера точки и её позиции
		
		private int x = 0;
		private int y = 0;
		private int pointNum = 0;
		
		/**
		 * Функция выставляет переданное число как координата по X
		 * Изменяет текущее состояние на указанное в параметре
		 * 
		 * @param xCoord	ожидаемая координата
		 * @throws 	Функция не вызывает исключения но в случае если координата <= 0 будет показано диалоговое 
		 * окно с предупреждением и состояние координаты не изменится 
		 */
		public void setXCoord(int xCoord) {
			if (xCoord > 0) {
				x = xCoord;
			} else {
				JOptionPane.showMessageDialog(jfss, "Не верная позиция курсора по Х координате");
			}
		}
		
		/**
		 * функция возвращает текущую координату по X
		 * @return	число > 0, координата по оси Х текущей точки
		 */
		public int getXCoord(){
			return x;
		}
		
		/**
		 * Функция выставляет переданное число как координата по Y
		 * Изменяет текущее состояние на указанное в параметре
		 * 
		 * @param yCoord	ожидаемая координата
		 * @throws 	Функция не вызывает исключения но в случае если координата <= 0 будет показано диалоговое 
		 * окно с предупреждением и состояние координаты не изменится 
		 */
		public void setYCoord(int yCoord) {
			if (yCoord > 0) {
				y = yCoord;
			} else {
				JOptionPane.showMessageDialog(jfss, "Не верная позиция курсора по Y координате");
			}
		}
		
		/**
		 * Функция возвращает текущую координату по Y
		 * @return	число > 0, текущая координата точки по оси Y
		 */
		public int getYCoord(){
			return y;
		}
		
		/**
		 * Функция устанавливает номер позиции, точки в списке точек упражнения
		 * Изменяет порядковый номер точки в списке точек упражнения
		 * 
		 * @param Num	число > 0 порядковый номер точки в упражнении
		 * @exception	Функция не вызывает исключение но если параметр передан <= 0 то будет
		 * показано сообщение и состояние не будет изменено
		 */
		public void setPointNum(int Num) {
			if (Num > 0) {
				pointNum = Num;
			} else {
				JOptionPane.showMessageDialog(jfss, "Не верный номер позиции");
			}
		}
		
		/**
		 * Функция возвращает номер позиции в списке упражнений
		 * @return	число > 0
		 */
		public int getPointNum(){
			return pointNum;
		}
		
		/**
		 * Конструктор точки. Устанавливает приватные переменные для дальнейшего использования
		 * @param xCoord	координата мышки по x оси
		 * @param yCoord	координата мышки по y оси
		 * @param nuumber	порядковый номер точке в массиве
		 */
		public Property (int xCoord, int yCoord, int nuumber) {
			setXCoord(xCoord);
			setYCoord(yCoord);
			setPointNum(nuumber);
		}
		
		/**
		 * Функция для человеко понятного описания состояния текущей точки.
		 * Необходимо для одинаковой отрисовки в списке всех точек упражнения.
		 * 
		 * Может быть изменена для нового отображения
		 * @return	возвращается подготовленная строка описания состояния точки
		 */
		public String getDisplayCoordinat(){
			String temp = "# = ";
			temp += ((Integer) pointNum).toString();
			temp += ";\t x coordinat = ";
			temp += ((Integer)x).toString();
			temp += ";\t y coordinat = ";
			temp += ((Integer)y).toString();
			return temp;
		}
	}
	/**
	 * Вспомогательный класс. Описываюший одно упражнение
	 * Содержит название и список точек в упражнении
	 */
	public class EyesSet implements java.io.Serializable {
		
		private String name = "";
		private Vector<Property> points = new Vector<Property>();
		private int countNum = 0;
		
		/**
		 * Функция которая устанавливает точку упраженения
		 * Увеличивает порядковый номер точек в списке на 1
		 * Добавляет точку в список точек
		 * 
		 * @param x,	x координата мышки
		 * @param y,	y координата мышки
		 */
		public void SavePointCoordinat (int x, int y) {
			countNum += 1;
			Property point = new Property(x, y, countNum);
			points.addElement(point);
		}
		
		/**
		 * Функция отрисовывает список координат на указанной панели. 
		 * Создаётся набор текстовых меток с координатами точки и номером точки в тексте
		 * 
		 * @param paintDesc 	панель на которой буддет отрисованн набор всех меток.
		 */
		public void DoShowPoints (JPanel paintDesc, Property p) {
			paintDesc.setLayout(null);
			for (Property var : points) {
				JLabel label = new JLabel(((Integer)var.pointNum).toString());
				
				label.setForeground(Color.red);
				label.setBounds(var.x, var.y, 20, 20);
				paintDesc.add(label);
				label.setVisible(true);
				paintDesc.repaint();
			}
		}
		
		/**
		 * Функция убирает все введённые точки в списке и обнуляет порядковый номер точек
		 */
		public void CleanSettings() {
			points.removeAllElements();
			countNum = 0;
		}
		
		/**
		 * Функция для получения названия упражнения
		 * @return	строка название упражнения
		 */
		public String GetSetName() {
			return name;
		}
		
		/**
		 * Функция возвращает набор всех точек
		 * @return	вектор набор точек текущего упражнения
		 */
		public Vector<Property> GetPoints() {
			return points;
		}
		
		/**
		 * Функция возвращающая количество точек в упражнении
		 * @return	число количество точек
		 */
		public int GetPointsCount() {
			return countNum;
		}
		
		/**
		 * Функция для изменения названия упражнения
		 * @param Name	строка название упражнения
		 */
		public void SetSetName(String Name) {
			name = Name;
		}
		
		/**
		 * Функция для сохранения списка всех точек упражнения
		 * @param Points	список набор всех точек упражнения
		 */
		public void SetPoints(Vector<Property> Points) {
			points = Points;
		}
		
		/**
		 * Функция устанавливает следующий номер точки.
		 * @param Count	число количество введённых точек
		 */
		public void SetPointsCount(int Count) {
			countNum = Count;
		}
	}
	
	private JFrame jfss = new JFrame("Eyes sets");
	private Vector<EyesSet> TaskList = new Vector<EyesSet>(); 
	private EyesSet sets = null;
	private Property point = null;
	
	private JEditorPane jeName = new JEditorPane(); // название упражнения
	private JList jlSet = new JList(new Vector<Property>()); // описание выбранного упражнения
	private JList jlSets = new JList(TaskList); // все упражнения
	private JButton jbSave = new JButton("Save set"); // сохранить упражнение
	private JButton jbClean = new JButton("Clean set"); // очистить упражнение
	private JButton jbClose = new JButton("Close"); // закрыть панель
	private JPanel  jpAction = new JPanel();
	private JPanel  jpMove = new JPanel();
	private JPanel  jpHelp = new JPanel();
	private JScrollPane jsTask = new JScrollPane(); // Панель с прокурткой для списка задач
	private JScrollPane jsPoint = new JScrollPane(); // Панель с прокруткой для списка точек в задаче
	private JPopupMenu jpmSetActions = new JPopupMenu(); // Высплывающая панель работы с выбраным упражнением (удалить)
	private JMenuItem jmiDeleteSet = new JMenuItem(); // кнопка удаления упражнения
	private JPopupMenu jpmPointActions = new JPopupMenu(); // Высплывающая панель работы с выбранной кнопкой
	private JMenuItem jmiDeletePoint = new JMenuItem(); // кнопка удаления точки
	

	/**
	 * Функция сериализует список всех введённых упражнений для глаз
	 * в файл output.bin 
	 * 
	 * @param ser	набор всех упражнений для глаз
	 * @throws Exception	IOException - ошибка работы с файлом
	 * 						NotSerializableException - ошибка сериализации не сериализуемого класса
	 */
	public void writeSets(Vector<EyesSet>  ser) throws Exception{
        System.out.println("write objects:");
	    FileOutputStream fos=new FileOutputStream("output.bin");
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(ser);
        oos.close();
    }

	/**
	 * Функция загружает настройки из сериализованного файла
	 * Изменяет список всех упражнений в текущем классе
	 * 
	 * @return 	список всех упражнений
	 * @throws Exception	FileNotFoundException - ошибка отсутствия загружаемого файла
	 * 						EOFException	- ошибка обработки потока (поток достиг своего конца)
	 * 	
	 */
    @SuppressWarnings("unchecked")
	public Vector<EyesSet> readSets() throws Exception {
    	// загружаем все упражнения из сохранённого файла
    	
    	File f = new File("output.bin");
    	if (f.exists()) {
        
        System.out.println("Read objects:");
        FileInputStream fis=new FileInputStream("output.bin");
        ObjectInputStream ois=new ObjectInputStream(fis);

        Vector<EyesSet> readObject = (Vector<EyesSet>)ois.readObject();
        ois.close();
        return readObject;
    	} else {return new Vector<EyesSet>();}
    }
    
    /**
     * Функция освобождает выбранное упражнение и позволяет создавать или выбирать новое
     */
    private void CleanSelectedSet(){
    	sets = new EyesSet();
		jpMove.removeAll();
		jpMove.repaint();
		jlSet.setListData(sets.GetPoints());
    }

    /**
     * функция сохраняющая новое упражнение в списке упражнений
     */
    private void SaveNewSet(){
	   sets.SetSetName(jeName.getText());
	   TaskList.add(sets);
	   jlSets.setListData(TaskList);   	
    }
	
	/**
	 * Написана для {@link jbSave.addActionListener}
	 * <p>
	 * функция сробатывающая при нажатии кнопки Save
	 * Сохраняет новое упражнение или изменяет старое
	 * <p>
	 * Если в списке упражнений не выбрана ни одна запись то будет создана новая
	 * Если в списке выбрана 1 запись, будет произведена проверка и если элемент остался
	 * не изменным произайдет замена иначе будет создана новая запись
	 */
    private void DoSaveSet() {
	   if (jlSets.getSelectedIndex() > -1) {
		   if (sets.equals(TaskList.get(jlSets.getSelectedIndex()))) {
			   sets.SetSetName(jeName.getText());
			   TaskList.setElementAt(sets, jlSets.getSelectedIndex());
		   } else { SaveNewSet();}
	   } else {SaveNewSet();}
    }

    /**
     * убирает все элементы с заданной панели. перерисовывает чистую панель
     * задаёт новый список для отображения данных.
     * Создает новые элементы на панеле
     * @param panel
     * Панель на которой будут находится новые элементы
     * @param list
     * Список где будет отображаться информация по новым элементам на панели
     */
    private void doRefrershMovePanel(JPanel panel, JList list) {
    	panel.removeAll();
    	panel.repaint();
		list.setListData(sets.GetPoints());
        list.repaint();
		sets.DoShowPoints(panel, point);
    }
    	/**
	 * Функция перерисовывает все метки на форме
	 */
	private void DoRefreshDesk(){
    	if (jlSets.getSelectedIndex() != -1){
        	CleanSelectedSet();
            sets = TaskList.get(jlSets.getSelectedIndex());
            jeName.setText(sets.GetSetName());
            sets.DoShowPoints(jpMove, point);
    	}
	};
	/**
	 * Функция удаляет выбранное ранее упражнение из списка
	 * вместе со всеми точками и названиями принадлежащими данному упражнению
	 */
	private void DeleteSetTask() {
		//убираем из списка
		TaskList.remove(sets);
		jlSets.setListData(TaskList);
		// убираем все точки
		CleanSelectedSet();
		sets.CleanSettings();
		//стираем имя
		sets.SetSetName("");
		jeName.setText(sets.GetSetName());
		// очищаем список точек

		jlSet.setListData(sets.GetPoints());
		jlSet.repaint();
	}
	/**
	 * Процедура для определения каким цветом будет отображён объект в списке
	 * 
	 * @param isSelected
	 * Параметр, является отрисовываемый объект выбранным или нет
	 * @return
	 * процедура возвращает масств цветов
	 * <p>
	 * Нулевой элемент массива - цвет фона
	 * <p>
	 * Первый элемент массива - цвет текста
	 */
	private Color[] DrowRowColor(boolean isSelected){
		// переменные для определения цвета
		int front = 1;
		int back = 0;
		// по умолчанию чёрные буквы белый фон
		Color[] bfg = {Color.WHITE, Color.BLACK};
        //если ячейка выбрана
        if (isSelected) {
        	//голубой фон
             bfg[back] = Color.CYAN;
             //черные буквы
             bfg[front] = Color.BLACK;
         }
         return bfg;
	}
	/**
	 * процедура для переопределения порядкового номера точки в списке точек упражнения
	 */
	private void ReindexArrayPoints(){
		for (int i = 0; i < sets.GetPoints().size(); i++){
			if (sets.GetPoints().get(i).getPointNum() != (i+1)) sets.GetPoints().get(i).setPointNum(i+1);
		}
	}
	/**
	 * Инициализирующая функция класса
	 * Создаёт форму устанавливает позиции элементов
	 * задаёт события на объекты
	 * 
	 */
	public void initSetsSettings() { // инициализация формы
		jfss.setState(JFrame.NORMAL);
		jfss.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		jfss.setLayout(new BorderLayout());
		
		jpHelp.add(jbSave);
		jpHelp.add(jbClean);
		jpHelp.add(jbClose);

		/**
		 *  кнопка для сохранения упражнения в список
		 */
		jbSave.addActionListener(new java.awt.event.ActionListener()
		{
				/**
				 * Событие по сохранению упражнения
				 */
			   public void actionPerformed(ActionEvent e)
			   {
				   DoSaveSet();
				   CleanSelectedSet();
				   jeName.setText("");
			   }
		});
		/**
		 *  кнопка для создания нового упражнения
		 */
		jbClean.addActionListener(new java.awt.event.ActionListener()
		{
			/**
			 * Создает новый экземпляр упражнения и очищает список точек
			 */
			   public void actionPerformed(ActionEvent e)
			   {
				   CleanSelectedSet();
			   }
		});
		/**
		 *  кнопка для закрытия формы и сохранения настроек в файл
		 */
		jbClose.addActionListener(new java.awt.event.ActionListener()
		{
			   public void actionPerformed(ActionEvent e)
			   {
				   try {
					   // сериализовать список упражнений
					   writeSets(TaskList);
				   } catch (Exception e1) {
					   // вывод сообщения об ошибки
					   e1.printStackTrace();
				   }
				   // очищаем и убираем форму
				   jfss.setVisible(false);
				   jfss.dispose();
			   }
		});
		
		// задаём расположение названия упражнения
		jfss.add(jeName, BorderLayout.NORTH);
		// устанавливаем цвет для формы где указывается траектория
		jpMove.setBackground(Color.darkGray);
		// по центру оставляем панель где будем указывать движение глаз
		jfss.add(jpMove, BorderLayout.CENTER);
		// событие по созданию новой контрольной точки
		jpMove.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (sets == null) {
					sets = new EyesSet();};
				//перемещаем созданную точку
				if (point != null){
					Property p = new Property(evt.getX(), evt.getY(), point.getPointNum());
					sets.GetPoints().setElementAt(p, sets.GetPoints().indexOf(point));
					sets.GetPoints().remove(point);
					point = null;
					doRefrershMovePanel(jpMove, jlSet);
				// создаем новую точку		
				}else sets.SavePointCoordinat(evt.getX(), evt.getY());
				// перерисовываем точки
				sets.DoShowPoints(jpMove, point);
				// передаём точки в массив

				// передаём массив в лист для отрисовки
				jlSet.setListData(sets.GetPoints());
			}
		});
		// инициализация списка задач
		InitTaskList(); 		
		// инициализация списка точек и управляющих кнопок
		InitPointList();
		// устанавливаем позицию панели действий
		jfss.add(jpAction, BorderLayout.SOUTH);
		
		jfss.setVisible(true);
		jfss.setAlwaysOnTop(true);
		
		// пытаемся загрузить настройки упражнений
		try {
			TaskList = readSets();
		} catch (Exception e) {
			System.out.println("Error heppen on decode");
			e.printStackTrace();
		}
		jlSets.setListData(TaskList);
	};

	/**
	 * Функция инициализирующая список всех упражнений
	 * <p>
	 * Задает размер списка.
	 * Способ передачи выбранных элементов.
	 * Определяет как будет отрисовываться список элементов
	 * Также определяется события при выборе элемента
	 */
	private void InitTaskList() {
    // Процедура создающая список задач
		jlSets.setModel(new javax.swing.AbstractListModel() {
            public int getSize() { return TaskList.size(); }
            public Object getElementAt(int i) { return TaskList.get(i); }
        });
		// переопределяем отрисовку
		jlSets.setCellRenderer(new javax.swing.DefaultListCellRenderer(){
			public Component getListCellRendererComponent(JList list, 
                    Object value,
                    int index, boolean isSelected,
                    boolean cellHasFocus) {
			
			EyesSet eye_s = (EyesSet)value;
			setText(eye_s.GetSetName());
			
			// определяем набор цветов
			Color[] bf = DrowRowColor(isSelected);
	         // меняем цвета	
	         setBackground(bf[0]);
	         setForeground(bf[1]);
			return this;
			}
		});
		
		jlSets.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		// задаём событее при нажатии мышки
		jlSets.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            		DoRefreshDesk();

                    jlSet.setListData(sets.GetPoints());
                    jlSet.repaint();
	                if ((evt.getButton() == MouseEvent.BUTTON3)&(jlSets.getSelectedIndex() != -1)){
	                	jmiDeleteSet.setText("Delete " + sets.GetSetName());
	                	jpmSetActions.show(evt.getComponent(), evt.getX(), evt.getY());
            	};   
            };
        });
		
		jlSets.addKeyListener(new java.awt.event.KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_DELETE){
					if (JOptionPane.showConfirmDialog(jfss, "Delete task " + sets.GetSetName()) == JOptionPane.YES_OPTION) DeleteSetTask();
				}
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
			
		});
		
		// создаём удаление упражнения
		jmiDeleteSet.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e) {
				DeleteSetTask();
			}
		});
		
		jsTask.setViewportView(jlSets);
		// задаём расположение списка всех упражнений
		jsTask.setPreferredSize(new Dimension(200, 700));
		jfss.add(jsTask, BorderLayout.EAST); 
		
		jpmSetActions.add(jmiDeleteSet);
	};
	
	private void DoDeletePoint(){
		if (JOptionPane.showConfirmDialog(jfss, "Delete point " + point.getDisplayCoordinat()) == JOptionPane.YES_OPTION){
			sets.GetPoints().removeElement(point);
			sets.SetPointsCount(sets.GetPointsCount()-1);
			ReindexArrayPoints();
			point = null;
			
			doRefrershMovePanel(jpMove, jlSet);
			sets.DoShowPoints(jpMove, point);
		}
	};
	/**
	 * Функция инициализирующая список точек в упражнении и кнопке действий
	 * <p>
	 * Создаёт список для отрисовки точек упражнения
	 * Переопределяет способ отрисовки
	 * устанавливает позицию списка на панеле
	 * добавляет кнопки на панель
	 */
	private void InitPointList() {
	    // Процедура создающая список задач
			
			jlSet.setModel(new javax.swing.AbstractListModel() {
	            public int getSize() { return new Vector<Property>().size(); }
	            public Object getElementAt(int i) {return sets.GetPoints().get(i);};
	        });
			
			jlSet.addMouseListener(new java.awt.event.MouseAdapter() {
	            public void mouseClicked(java.awt.event.MouseEvent evt) {
	            	if (sets != null) {
	            		point = sets.GetPoints().get(jlSet.getSelectedIndex());
	            		jpMove.getGraphics().drawOval(point.getXCoord() - 8, point.getYCoord() - 3, 25, 25);
	            		
	            		jmiDeletePoint.setText("Delete point number " + point.getPointNum());
	                    if ((evt.getButton() == MouseEvent.BUTTON3)&(jlSet.getSelectedIndex() != -1)){
		                	jpmPointActions.show(evt.getComponent(), evt.getX(), evt.getY());
	                    }
	            	}
	            };
	        });
			
			jlSet.addKeyListener(new java.awt.event.KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_DELETE){
						point = sets.GetPoints().get(jlSet.getSelectedIndex());
						DoDeletePoint();

					}
				};

				@Override
				public void keyReleased(KeyEvent e) {
		
				}

				@Override
				public void keyTyped(KeyEvent e) {
				
				}
				
			});

			jlSet.setCellRenderer(new javax.swing.DefaultListCellRenderer(){
				public Component getListCellRendererComponent(JList list, 
	                    Object value,
	                    int index, boolean isSelected,
	                    boolean cellHasFocus) {
					
				Property prop = (Property)value;
				setText(prop.getDisplayCoordinat());
				
				// определяем набор цветов
				Color[] bf = DrowRowColor(isSelected);
		        // меняем цвета	
		        setBackground(bf[0]);
		        setForeground(bf[1]);
				return this;
				}
			});
			
			jlSet.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

			jsPoint.setViewportView(jlSet);
			jpAction.setLayout(new BorderLayout());
			jpAction.add(jsPoint, BorderLayout.NORTH); // задаём расположение списка всех упражнений
			jpAction.add(jpHelp, BorderLayout.SOUTH);
			
			jmiDeletePoint.addActionListener(new java.awt.event.ActionListener(){
				public void actionPerformed(ActionEvent e) {
					point = sets.GetPoints().get(jlSet.getSelectedIndex());
					DoDeletePoint();
				}
			});
			
			jpmPointActions.add(jmiDeletePoint);
		};
	
}
