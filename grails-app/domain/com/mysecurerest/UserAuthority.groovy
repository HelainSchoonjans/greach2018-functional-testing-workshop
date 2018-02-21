package com.mysecurerest

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.codehaus.groovy.util.HashCodeHelper
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@ToString(cache=true, includeNames=true, includePackage=false)
class UserAuthority implements Serializable {

	private static final long serialVersionUID = 1

	User user
	Authority authority

	@Override
	boolean equals(other) {
		if (other instanceof UserAuthority) {
			other.userId == user?.id && other.authorityId == authority?.id
		}
	}

    @Override
	int hashCode() {
	    int hashCode = HashCodeHelper.initHash()
        if (user) {
            hashCode = HashCodeHelper.updateHash(hashCode, user.id)
		}
		if (authority) {
		    hashCode = HashCodeHelper.updateHash(hashCode, authority.id)
		}
		hashCode
	}

	static UserAuthority get(long userId, long authorityId) {
		criteriaFor(userId, authorityId).get()
	}

	static boolean exists(long userId, long authorityId) {
		criteriaFor(userId, authorityId).count()
	}

	private static DetachedCriteria criteriaFor(long userId, long authorityId) {
		UserAuthority.where {
			user == User.load(userId) &&
			authority == Authority.load(authorityId)
		}
	}

	static UserAuthority create(User user, Authority authority, boolean flush = false) {
		def instance = new UserAuthority(user: user, authority: authority)
		instance.save(flush: flush)
		instance
	}

	static boolean remove(User u, Authority r) {
		if (u != null && r != null) {
			UserAuthority.where { user == u && authority == r }.deleteAll()
		}
	}

	static int removeAll(User u) {
		u == null ? 0 : UserAuthority.where { user == u }.deleteAll() as int
	}

	static int removeAll(Authority r) {
		r == null ? 0 : UserAuthority.where { authority == r }.deleteAll() as int
	}

	static constraints = {
	    user nullable: false
		authority nullable: false, validator: { Authority r, UserAuthority ur ->
			if (ur.user?.id) {
				if (UserAuthority.exists(ur.user.id, r.id)) {
				    return ['userRole.exists']
				}
			}
		}
	}

	static mapping = {
		id composite: ['user', 'authority']
		version false
	}
}
