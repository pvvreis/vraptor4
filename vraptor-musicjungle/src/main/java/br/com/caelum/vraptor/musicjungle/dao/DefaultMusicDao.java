/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.caelum.vraptor.musicjungle.dao;

import static org.hibernate.criterion.MatchMode.ANYWHERE;
import static org.hibernate.criterion.Restrictions.ilike;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;

import br.com.caelum.vraptor.musicjungle.model.Music;
import br.com.caelum.vraptor.musicjungle.model.MusicOwner;

/**
 * Default implementation for MusicDao. <br> Annotating 
 * this class with <code>@Component</code> we have the 
 * dependency injection support either on this class and 
 * on other classes that depend on MusicDao or DefaultMusicDao
 *
 * @author Lucas Cavalcanti
 * @author Rodrigo Turini
 */
public class DefaultMusicDao implements MusicDao {

	private EntityManager entityManager;

	//CDI eyes only
	@Deprecated
	public DefaultMusicDao() {
	}
	
	/**
	 * Creates a new MusicDao.
	 *
	 * @param entityManager JPA's EntityManager.
	 */
	@Inject
	public DefaultMusicDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void add(Music music) {
		getSession().save(music);
	}

	@Override
	public void add(MusicOwner copy) {
		getSession().save(copy);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Music> searchSimilarTitle(String title) {
		// creates a criteria based on the Music class and adds
		// the "title" restriction and then returns the list.
		Criteria criteria = getSession().createCriteria(Music.class);
		return criteria.add(ilike("title", title, ANYWHERE)).list();
	}
	
	@Override
	public Music load(Music music) {
		return (Music) getSession().load(Music.class, music.getId());
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Music> listAll() {
		return getSession().createCriteria(Music.class).list();
	}
	
	private Session getSession() {
		return entityManager.unwrap(Session.class);
	}

}
