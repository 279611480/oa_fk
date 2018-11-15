package org.yun.menu.service.impl;


import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.yun.common.data.domain.Result;
import org.yun.identity.UserHolder;
import org.yun.identity.domain.Role;
import org.yun.identity.domain.User;
import org.yun.identity.repository.RoleDao;
import org.yun.identity.repository.UserDao;
import org.yun.menu.domain.Menu;
import org.yun.menu.repository.MenuDao;
import org.yun.menu.service.MenuService;



@Service
public class MenuServiceImpl implements MenuService {
	
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private UserDao userDao;
	/**
	 * menu是页面  传过来的菜单  
	 * old是从数据库里面查找出来的  菜单数据
	 * */
	@Override
	public void save(Menu menu) {
		//如果说 页面传过来的 保存的  菜单  的数据 id为空，那么，就将它id设置为空
		if (StringUtils.isEmpty(menu.getId())) {
			menu.setId(null);
		}
		
		//如果说，页面上有个菜单，且  他的父级的id为空 ，
		//  那就直接将他的父级属性设置为null   (表明都是新增的情况)
		if(menu.getParent() !=null && StringUtils.isEmpty(menu.getParent().getId())) {
			menu.setParent(null);
		}
		
		//1.检查相同的父菜单里面是否有同名的菜单
		//比如菜单管理下面，只能有一个【系统管理】
		Menu old;//old是从数据库查找出来的数据
		//表明 页面上要保存的菜单数据  有一个父级
		if(menu.getParent() != null) {//一个是如果有上级菜单，页面上要保存的菜单数据的  名称  怕与  数据库的  原有数据的同一父级下有相同的名称 
			//那么，就根据上级菜单查找看是否有重复的。 
			//调用持久层方法，将页面上菜单的数据传进去，（在父级里面，根据，页面menu传过来的name（菜单名字）与menu的(Parent)查找）
			//看看数据库里面是否有，相同的菜单数据（在同一个父级菜单里面）有没有与页面上传过来的  菜单数据名字相同的数据
			old = this.menuDao.findByNameAndParent(menu.getName(),menu.getParent());	
		}else {//一个是如果没有有上级菜单，页面上要保存的菜单数据的  名称  怕与  数据库的  原有数据的顶层父级有相同名字
			//如果说没有上一级(说明自己就是最顶级的上级菜单)，
			//则直接就去数据库找，（调用持久层，根据页面传进来的name，去数据库里面找数据）
			//看有没有菜单（顶级菜单名字）名字相同的数据（因为，数据库里面不能有顶级菜单名字相同的两个上级菜单）
			old = this.menuDao.findByNameAndParentNull(menu.getName());
		}
		
		//如果说  上面  第两个条件都过去了的话   就到这里  ，
		//如果说，没有上级且从数据库查找到的数据不为空，并且从数据库查找到的菜单数据（old）的Id与页面（menu）传进来的id,不相等，
		//说明，  页面上传过来的数据  与  数据库  原有的上级菜单数据的  名字  不同 ，那么抛出异常，
		//因为数据库不能有两个 其它数据  相同  但是就是name不同的情况
		if(old != null && !old.getId().equals(menu.getId()) ) {
			// 根据名称查询到数据库里面的菜单，但是两者的id不同
			throw new IllegalArgumentException("页面上传过来的数据与数据库里面的数据，id不同，"
					+ "说明他们的名字相同，但是数据库规定  两个上级菜单的名字不能相同，所以抛异常");//菜单的名字不能重复
		}
		
		//2。根据选取角色的ID,查询角色，解决角色的KEY重复的问题
		List<String> rolesIds = new LinkedList<>();//先定义一个接收页面传过来的角色id的List集合   
		//如果说  页面获得的角色为null   那么，直接将角色   那一栏设置为空   因为 不设置的话   会报空指针的错误
		if(menu.getRoles() ==null) {
			//如果if后面加上else{}  那么这句话  不用写   这个只是为了  避免 页面保存 没加角色  报空指针问题
			//如果说  页面,直接将角色   那一栏设置为空  （角色为空的话） 添加菜单 那么 会报空指针的错误   因为什么都没有
			menu.setRoles(new LinkedList<>());
		}
		//如果选择了角色 ，那么  将角色遍历出来   添加到 接收角色id的list集合
		menu.getRoles().forEach(role->rolesIds.add(role.getId()));
		//查询数据库里面所有的角色	List<Role> roles = this.roleDao.findAllById(roleIds);//此处查询出来，绝对不会有重复记录
		//Set集合   无序且不重复
		List<Role> roles = this.roleDao.findAllById(rolesIds);// 此处查询出来，绝对不会有重复记录
		Set<Role> set = new HashSet<>();
		set.addAll(roles);// 去重
		
		//将页面的角色清空，再将set去重后的角色加进去 ，免得重复角色
		menu.getRoles().clear();
		menu.getRoles().addAll(set);
		
		//3设置排序的序号（菜单可以拖动顺序）
		// 找到同级最大的number，然后加10000000，就形成一个新的number作为当前菜单的number
		// 如果是修改，则不需要查询
		
		//如果说  old（从数据库查到的菜单数据）不为空，与页面上新增菜单的数据  菜单 同级的情况下   
		//那么，新增菜单的数据的number（菜单排序序号）就是数据库里面查找到的最大number
		if(old != null) {//对应54行的old  有父级菜单的情况下
			menu.setNumber(old.getNumber());
		}else {//对应59行的old    没有父级菜单的情况下
			//否则的话，  那么先设置一个Double类型数据的maxNumber
			Double maxNumber;
			//如果  页面上的数据  没有上一级菜单  那么，maxNumber  就是原本的maxNumber
			//调用持久层方法，在parent为null的情况下，查询maxNumber
			if(menu.getParent() == null) {
				maxNumber = this.menuDao.findMaxNumberByParentNull();
			}else {
				//有父级 菜单的情况
				//不然话就去找，页面上  父级的maxNumber   传入menu（页面的parent）条件，
				//查询，父级菜单的maxNumber  设置为己有的maxNumber  
				maxNumber = this.menuDao.findMaxNumberByParent(menu.getParent());
			}
			if(maxNumber==null) {
				maxNumber =0.0;
			}
			Double number = maxNumber + 10000000.0;
			menu.setNumber(number);
		}
		
		//4.保存数据
		this.menuDao.save(menu);

	}

	@Override
	public List<Menu> findTopMenus() {
		
		return this.menuDao.findByParentNullOrderByNumber();
	}
	
	
	/**
	 * zTree三种拖动状态
	 * inner   表示 拖到  某个级的节点  的 下一级 节点里面
	 * prev  表示都处于同一级  但是 拖动到  目标节点的前面
	 * next  也是同一级的情况   但是拖到目标节点的前面
	 *   
	 * **/
	
	
	
	//~~~~~如果说，inner 与 moveType相同   表明要将  某个节点（菜单）  插入到某个节点下面
	//那么，（有父级的情况下）
	//调用持久层方法根据 最大的排序号（maxNumber） 在有父级的情况下查询数据库里面 的那个节点的最大排序号    
	//判断  如果在 没有父级的情况下  那么直接将  上面查到的最大排序号 设置为0.0  表示自己就是最顶级的父级  
	//不然就把排序号  在最大排序号的前提下面  加上10000000.0
	//~~~~~不然的话  当 prev等于moveType（移动时候的）	
//			表明要将节点加入  某节点中间 （// number应该小于target的number，并且大于target前一个菜单的number）	
	// 查询数据库里面的第一页的第一条数据		
	//调用持久层方法，根据 	// number应该小于target的number，并且大于target前一个菜单的number   条件查询   
//			将目标节点的父级，目标节点的排序号，第一条数据作为形参传进去
	//拿到目标节点的number（排序号）		
	//判断，如果要移动的节点（多条）的排序号大于0  （虽然说之前已经禁用了使用Ctrl去禁用  可以选多条数据  但是zTree在
			//设计的时候就设计了可以多选节点  所以需要拿到第一个节点  才是自己想要的节点）
			//那么拿到  prev的排序号（拿索引为0的那个节点  就是第一个）
			//将要移动节点的排序号设置为   目标节点排序号与  prev 节点排序号  相加的中间哪里
			//不然的话，就将要移动节点的序列号除以2
	//最后将页面上的number设置为，修改过后的Number
			//设置，要移动节点的父级设置为与目标节点  处于同一级下面
	//~~~~不然的话
			//如果，要next与移动的类型相同
			//那么，情况应该是// number应该大于target的number，并且小于target后一个菜单的number
			//其他与 prev相同
	//不然的话，抛出异常  非法的菜单移动异常
			
	//循环结束后返回OK状态
	@Override
	@Transactional
	public Result move(String id, String targetId, String moveType) {
		//调用持久层方法 根据id  去查找数据  没有话调用or.else（null）返回null 
		Menu menu = this.menuDao.findById(id).orElse(null);
		
		//移动的重点：重新计算number（排列序号），并且要修改parent
		if(StringUtils.isEmpty(targetId)) {//如果说，目标节点为空的话（没有目标节点）
				//那么，一定是移动到所有一级菜单后面去
			Double maxNumber = this.menuDao.findMaxNumberByParentNull(); 
			if(maxNumber==null) {
				maxNumber=0.0;
			}
			Double number = maxNumber +10000000.0;;
			menu.setNumber(number);
			menu.setParent(null);
			return Result.ok();
		}
		
		//调用持久层方法  根据传过来的目标id（targetId），查找数据 没有的话返回null 
		Menu target = this.menuDao.findById(targetId).orElse(null);

		if("inner".equals(moveType)) {//inner的情况
			//把menu移动到target（目标）中里面，此时的menu的parent直接改为target即可
			//number则是根据target作为父菜单，找到最大的number，然后加上一个数字
			Double maxNumber = this.menuDao.findMaxNumberByParent(target);
			if(maxNumber == null) {			
				maxNumber = 0.0;
			}
			Double number = maxNumber + 10000000.0;
			menu.setParent(target);
			menu.setNumber(maxNumber);
		}else if("prev".equals(moveType)) {//prev的情况
			//numebr应该是小于target的number,并且大于target前一个菜单的number
			//因为zTreep在设计之初 ，就已经设计了，可以多选（以数组的形式保存），所说前面虽然已经禁用了使用Ctrl多选，
			//但是人家的数组依旧是存在的所以需要，根据索引拿到自己需要移动的节点
			Pageable pageable = PageRequest.of(0, 1);//查询第一页，只要第一个数据
			//调用持久层方法，根据传进去的，目标节点的父级/排序号   与  自己要移动的节点数据 （上面查到的 ） ，查到  要过去的位置
			//Desc是升序排序  1 2 3 4 5  因为只要第一个数据
			Page<Menu> prevs = this.menuDao
					.findByParentAndNumberLessThanOrderByNumberDesc(target.getParent(),target.getNumber(),pageable);
			//拿到目标节点的排序号
			Double next = target.getNumber();
			//定义自己的排序号
			Double number;
			//判断：如果  自己从上面索引（194）从索引里面 拿到的第一条数据，是大于0的，说明拿到了数据
			//那么，将它的排序号设置为，
			//目标节点拿到的排序号与从索引里面拿到的要移动的节点的内容的排序号  加起来/2  就是要移动过去的节点位置
			if(prevs.getNumberOfElements()>0) {
				Double prev = prevs.getContent().get(0).getNumber();//拿到了，要移动节点的数据的排序号
				number = (next + prev)/2;//（1  2）放在   2节点之前的时候  2前面有1节点   所以需要拿到1 2 节点的排序号 然后相加除以2就是要放的位置了     
			}else {//除此以外，说明，要移动过去的位置是，目标节点（next 上面定义的），
				//的前面已经是没有节点的了 ，那么直接将目标节点的排序号除以2就是要移动过去的位置
				number = next/2;//（2）  放在2节点之前   前面没有（1）节点   ，那么直接将目标节点的排序号除以2  就是往前设置了
			}
			//将页面上的节点number设置为number（上面处理完的排序号）
			menu.setNumber(number);
			//移动到目标节点（target）前面，但是  它是和  目标节点  是处于同一级节点的
			menu.setParent(target.getParent());
		}else if("next".equals(moveType)) {//如果说  next 与 moveType的类型相同 那么
			//number  应该大于target的number且小于target后一个菜单的number   但是 还是处于同级的情况
			Pageable pageable = PageRequest.of(0, 1);// 查询第一页、只要1条数据
			//（因为，某一级菜单下面可能有很多的子级菜单（如：生物的菜单（节点）下面有很多子节点，
			//子节点下面又有很多的子节点）），那么在数据库里面就显示了很多页，所以我们只需要分页就行了，
			//拿到第一条数据（目标节点），就是我们想要的数据
			Page<Menu> prevs = this.menuDao//
					.findByParentAndNumberGreaterThanOrderByNumberAsc(target.getParent(), target.getNumber(), pageable);

			Double prev = target.getNumber();
			Double number;
			if (prevs.getNumberOfElements() > 0) {
				Double next = prevs.getContent().get(0).getNumber();
				number = (next + prev) / 2;//（1  2）放在   1节点之前的时候  1后面有2节点   所以需要拿到1 2 节点的排序号 
				//然后相加除以2就是要放的位置了     
			} else {//（1）  放在1节点之后   前面没有（2）节点 ，那么直接将  序列号设置为  目标节点之后就行了  +排序号即可  
				number = prev + 10000000.0;
			}
			menu.setNumber(number);
			// 移动到target之后，跟target同级
			menu.setParent(target.getParent());
		} else {
			throw new IllegalArgumentException("非法的菜单移动类型，只允许inner、prev、next三选一。");
		}

		return Result.ok();
	}

	@Override
	public Result delete(String id) {
		// 直接调用deleteById的方法，在数据库没有对应的记录时会抛出异常
//		this.menuRepository.deleteById(id);

		// 当对象不存在，会先插入一条，然后再删除！
//		Menu entity = new Menu();
//		entity.setId(id);
//		this.menuRepository.delete(entity);
		
		
		//先调用持久方法，根据id查询数据库里面的数据，看有没有要删除的节点的数据，没有的话，就返回null
		Menu entity = this.menuDao.findById(id).orElse(null);
		//如果  entity(要删除的菜单节点) 不为空的话 ，
		if(entity != null) {
			//判断：如果没有下一级菜单的话，直接调用持久层方法，删除节点
			if(entity.getChilds().isEmpty()) {
				this.menuDao.delete(entity);
			}else {//不然的话，说明是有节点的，那么，直接返回错误信息
				return Result.error();
			}
		}
		//如果说，要删除的菜单节点为空的话(entity)，那么就直接返回，删除成功的状态
		return Result.ok();
	}

	@Override
	public List<Menu> finMyMenus() {
		//拿到从线程里面的User是瞬态的，不是持久化状态的
		User user = UserHolder.get();
		//拿到持久化状态的User
		//【调用用户持久层.getOne()方法,将上面拿到的user传入当做参数，查询数据库有没有对应的user】
		user = userDao.getOne(user.getId());
		//持久化状态user，调用方法拿到所有角色【因为可能传入不同的用户，找到它们所对应的角色】
		//， 拿个list集合接收
		List<Role> roles = user.getRoles();
		
		/**
		 *根据roles集合，查询所有的菜单，得到用户有权限访问的菜单
		 *此时得到的菜单，可能只包括一级。二级、三级.....等所有可能存在的菜单
		 *左侧的菜单树，其实只要两级（一级、二级）即可，并且应该返回一级、在一级里面包含二级
		 *1.根据	角色，查询到所有的（对应的）菜单	 【调用菜单持久层方法查询角色In】
		 * */
		List<Menu> menus = this.menuDao.findByRolesIn(roles);//使用in查询
		//List<Menu> menus = this.menuDao.findDistinctMenuByRolesIn(roles);//使用in查询
		/**创建一个LinkList集合  接收，准备返回的一级菜单*/
		List<Menu> topMenus = new LinkedList<>();
		/**
		 *  找到所有的一级菜单，放入topMenus
		 *循环所有的二级、三级...菜单，
		 *以parent【一级菜单】作为Key放入map里面，value是一个集合表示parent对应所有权的下一级 
		 * */
		Map<Menu, List<Menu>> map = new HashMap<>();
		/**
		 * 此循环结束后，Map里面的数据，包括所有的上级、下级的菜单，并且是有权限的
		 * 2.建立一级菜单和二级菜单的对应关系
		 *使用流式API写法  返回上面所需要的
		 * */
		menus.stream()
				.filter(menu->menu.getParent() != null)//表示，下级菜单。
				/**遍历循环拿到它的父级菜单，而且判断，
				 * 父级菜单是不是数据库（map）拥有了，没的话就加入，有的话，拿到父级菜单，将自己菜单设置进去
				*/
				.forEach(menu->{
					//拿到上一级菜单
					Menu parent = menu.getParent();
					//判断   如果说上级菜单不是，上面使用线程用户拿到的父级菜单集合里面  
					if(!map.containsKey(parent)) {
						//那就把它放进去
						map.put(parent, new LinkedList<>());
					}
					//不然的话，说明是，map里面的父级菜单，那就把它所对应的下级菜单，加入父级菜单里面
					List<Menu> childs = map.get(parent);
					childs.add(menu);//把当前菜单加入对应的上级菜单去
				});
				
			//在内存里面，对菜单进行排序，以序号为排序的依据   不排序的话，每次刷新页面，菜单 

			Comparator<Menu> comparator=(menu1,menu2)->{
				if(menu1.getNumber() > menu2.getNumber()) {
					return 1;
				}else if(menu1.getNumber() < menu2.getNumber()) {
					return -1;
				}else {
					return 0;
				}
			};
			/**3.构建新的一级菜单，并且把二级菜单放入一级菜单里面*/
			map.entrySet().stream()
							//判断Key没有上级的菜单，表示一级菜单
							.filter(entry->entry.getKey().getParent() == null)
							//那么，把它遍历出来
							.forEach(entry->{
								//拿到一级菜单
								Menu parent = entry.getKey();
								//拿到二级菜单，且用List集合接收
								List<Menu> 二级菜单 = entry.getValue();
								//查询得到的是持久化对象，不要修改它们
								//返回菜单的时候，由于需要通过权限组装数据，所以是需要修改Menu对象
								//因此创建一个新的Menu ,避免被意外修改
								Menu menu = this.copy(parent);//拿到一级菜单
								//forEach循环遍历二级菜单
								二级菜单.forEach(child->{
									Menu subMenu = this.copy(child);
									menu.getChilds().add(subMenu);//将二级菜单加入到一级菜单去												
								});
								//排序二级菜单
								//System.out.println(menu.getChilds()+"----------------------------------------");
								menu.getChilds().sort(comparator);
								//把一级菜单加入返回的集合里面去
								topMenus.add(menu);	
							});	
			//排序一级菜单
			topMenus.sort(comparator);
			//返回的菜单集合			
			return topMenus;
	}
	/**
	 * 提取出来的方法  被上面调用
	 * 
	 * 传入持久化对象，返回瞬态对象
	 * param persist 数据库查询得到的
	 * @return 瞬态的
	 * */
	private Menu copy(Menu persist) {
		Menu menu =  new Menu();//瞬态对象
		//将从数据库里面找到的菜单Id,Name,Number,Url,Childs设置到瞬态对象去，以免修改数据库的信息
		menu.setId(persist.getId());
		menu.setName(persist.getName());
		menu.setNumber(persist.getNumber());
		menu.setUrl(persist.getUrl());
		menu.setChilds(new LinkedList<>());
		//返回瞬态对象
		return menu;
	}
}
