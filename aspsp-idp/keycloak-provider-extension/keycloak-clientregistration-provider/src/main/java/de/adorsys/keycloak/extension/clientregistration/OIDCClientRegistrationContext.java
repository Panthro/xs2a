/*
 * Copyright (C) 2018 adorsys GmbH & Co. KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.adorsys.keycloak.extension.clientregistration;

import org.apache.commons.lang3.StringUtils;
import org.keycloak.models.KeycloakSession;
import org.keycloak.protocol.oidc.utils.SubjectType;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.oidc.OIDCClientRepresentation;
import org.keycloak.services.clientregistration.AbstractClientRegistrationContext;
import org.keycloak.services.clientregistration.ClientRegistrationProvider;
import org.keycloak.services.validation.PairwiseClientValidator;
import org.keycloak.services.validation.ValidationMessages;

import java.util.HashSet;
import java.util.Set;

public class OIDCClientRegistrationContext extends AbstractClientRegistrationContext {

	private final OIDCClientRepresentation oidcRep;

	public OIDCClientRegistrationContext(KeycloakSession session, ClientRepresentation client,
			ClientRegistrationProvider provider, OIDCClientRepresentation oidcRep) {
		super(session, client, provider);
		this.oidcRep = oidcRep;
	}

	@Override
	public boolean validateClient(ValidationMessages validationMessages) {
		boolean valid = super.validateClient(validationMessages);

		String rootUrl = client.getRootUrl();
		Set<String> redirectUris = new HashSet<>();
		if (client.getRedirectUris() != null) {
			redirectUris.addAll(client.getRedirectUris());
		}

		SubjectType subjectType = SubjectType.parse(oidcRep.getSubjectType());
		String sectorIdentifierUri = oidcRep.getSectorIdentifierUri();

		// If sector_identifier_uri is in oidc config, then always validate it
		if (SubjectType.PAIRWISE == subjectType || StringUtils.isNotBlank(sectorIdentifierUri)) {
			valid = valid && PairwiseClientValidator.validate(session, rootUrl, redirectUris,
					oidcRep.getSectorIdentifierUri(), validationMessages);
		}
		return valid;
	}
}
